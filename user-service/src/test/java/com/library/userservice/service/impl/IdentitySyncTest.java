package com.library.userservice.service.impl;
import com.library.userservice.dto.IdentityUpdateDTO;
import com.library.userservice.entity.UserProfile;
import com.library.userservice.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class IdentitySyncTest {
    @Mock UserRepository repository;
    @InjectMocks UserServiceImpl service;
    @Test void preservaDatosDelPerfilAlActualizarIdentidad() {
        UserProfile profile = new UserProfile(42L, "Nombre", "viejo@example.com", "912345678", "Dirección 123");
        when(repository.findByEmail("viejo@example.com")).thenReturn(Optional.of(profile));
        when(repository.save(any())).thenAnswer(call -> call.getArgument(0));
        var result = service.updateIdentity("viejo@example.com", new IdentityUpdateDTO("Nuevo", "nuevo@example.com"));
        assertEquals(42L, result.getId()); assertEquals("912345678", result.getPhone());
        assertEquals("Dirección 123", result.getAddress()); assertEquals("nuevo@example.com", result.getEmail());
    }
    @Test void borrarPerfilAusenteEsIdempotente() {
        service.deleteUserByEmail("ausente@example.com");
        verify(repository).deleteByEmail("ausente@example.com");
    }
    @Test void noSobrescribeOtroPerfil() {
        when(repository.existsByEmail("ocupado@example.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.updateIdentity("viejo@example.com", new IdentityUpdateDTO("Nuevo", "ocupado@example.com")));
        verify(repository, never()).save(any());
    }
}
