package com.library.authservice.service;

import com.library.authservice.dto.*;
import com.library.authservice.entity.*;
import com.library.authservice.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdminAccountServiceTest {
    @Mock UserRepository repository;
    @Mock UserService registration;
    @Mock PasswordEncoder encoder;
    @Mock RestTemplate remote;
    @InjectMocks AdminAccountService service;
    User account;
    AdminAccountRequest request;

    @BeforeEach void setup() {
        account = new User(7L, "Persona", "persona@example.com", "hash", Role.CLIENT);
        request = new AdminAccountRequest();
        request.setName("Nombre editado"); request.setEmail("nuevo@example.com"); request.setRole(Role.ADMIN);
    }
    @Test void listaSinContraseñasConIdDeAuth() {
        when(repository.findAll()).thenReturn(List.of(account));
        assertEquals(new AdminAccountResponse(7L, "Persona", "persona@example.com", Role.CLIENT), service.list().get(0));
    }
    @Test void creaRolSolicitadoUsandoRegistroYPasswordCifrada() {
        request.setPassword("clave123");
        when(registration.registerUser(any())).thenReturn(new UserResponseDTO(7L, "Persona", "persona@example.com"));
        when(repository.findById(7L)).thenReturn(Optional.of(account));
        when(repository.save(account)).thenReturn(account);
        assertEquals(Role.ADMIN, service.create(request).role());
        verify(registration).registerUser(argThat(data -> data.getPassword().equals("clave123")));
    }
    @Test void altaExigeContraseña() {
        assertThrows(ResponseStatusException.class, () -> service.create(request));
        verifyNoInteractions(registration);
    }
    @Test void editaYRelacionaPerfilPorCorreoNoPorId() {
        when(repository.findById(7L)).thenReturn(Optional.of(account));
        when(repository.save(account)).thenReturn(account);
        service.update(7L, request, "admin@example.com");
        verify(remote).put(eq("http://user-service:8080/users/email/{email}"), anyMap(), eq("persona@example.com"));
        assertEquals("nuevo@example.com", account.getEmail());
        assertEquals(Role.ADMIN, account.getRole());
        assertEquals("hash", account.getPassword());
        verifyNoInteractions(encoder);
    }
    @Test void passwordNuevaSeCifra() {
        when(repository.findById(7L)).thenReturn(Optional.of(account));
        when(repository.save(account)).thenReturn(account);
        request.setPassword("nueva123");
        when(encoder.encode("nueva123")).thenReturn("nuevo-hash");
        service.update(7L, request, "admin@example.com");
        assertEquals("nuevo-hash", account.getPassword());
    }
    @Test void rechazaCorreoDuplicadoAntesDeSincronizar() {
        when(repository.findById(7L)).thenReturn(Optional.of(account));
        when(repository.existsByEmail("nuevo@example.com")).thenReturn(true);
        assertEquals(409, assertThrows(ResponseStatusException.class, () -> service.update(7L, request, "admin@example.com")).getStatusCode().value());
        verifyNoInteractions(remote);
    }
    @Test void noConfirmaEdicionSiFallaElPerfil() {
        when(repository.findById(7L)).thenReturn(Optional.of(account));
        doThrow(new ResourceAccessException("offline")).when(remote).put(anyString(), anyMap(), anyString());
        assertThrows(RestClientException.class, () -> service.update(7L, request, "admin@example.com"));
        verify(repository, never()).save(any());
        assertEquals("persona@example.com", account.getEmail());
    }
    @Test void eliminaPerfilAntesDeCuenta() {
        when(repository.findById(7L)).thenReturn(Optional.of(account));
        service.delete(7L, "admin@example.com");
        InOrder order = inOrder(remote, repository);
        order.verify(remote).delete("http://user-service:8080/users/email/{email}", "persona@example.com");
        order.verify(repository).delete(account);
    }
    @Test void conservaCuentaSiFallaEliminarPerfil() {
        when(repository.findById(7L)).thenReturn(Optional.of(account));
        doThrow(new ResourceAccessException("offline")).when(remote).delete(anyString(), anyString());
        assertThrows(RestClientException.class, () -> service.delete(7L, "admin@example.com"));
        verify(repository, never()).delete(any());
    }
    @Test void protegeCuentaPropiaYPrincipal() {
        when(repository.findById(7L)).thenReturn(Optional.of(account));
        assertThrows(ResponseStatusException.class, () -> service.delete(7L, account.getEmail()));
        request.setRole(Role.CLIENT);
        assertThrows(ResponseStatusException.class, () -> service.update(7L, request, account.getEmail()));
        account.setEmail("admin@library.cl");
        assertThrows(ResponseStatusException.class, () -> service.delete(7L, "otro@example.com"));
        verifyNoInteractions(remote);
    }
    @Test void inexistenteEs404() {
        assertEquals(404, assertThrows(ResponseStatusException.class, () -> service.get(99L)).getStatusCode().value());
    }
}
