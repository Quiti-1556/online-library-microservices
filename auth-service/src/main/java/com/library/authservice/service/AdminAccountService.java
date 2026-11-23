package com.library.authservice.service;

import com.library.authservice.dto.*;
import com.library.authservice.entity.*;
import com.library.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminAccountService {
    private final UserRepository repository;
    private final UserService registration;
    private final PasswordEncoder encoder;
    private final RestTemplate restTemplate;

    private AdminAccountResponse response(User account) {
        return new AdminAccountResponse(account.getId(), account.getName(), account.getEmail(), account.getRole());
    }
    private User account(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuenta no encontrada"));
    }
    public List<AdminAccountResponse> list() {
        return repository.findAll().stream().map(this::response).toList();
    }
    public AdminAccountResponse get(Long id) { return response(account(id)); }

    @Transactional
    public AdminAccountResponse create(AdminAccountRequest request) {
        if (request.getPassword() == null || request.getPassword().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña inicial es obligatoria");
        // Reutiliza registro: contraseña cifrada y creación del perfil en user-service.
        RegisterRequestDTO data = new RegisterRequestDTO(request.getName().trim(),
                request.getEmail().trim().toLowerCase(Locale.ROOT), request.getPassword());
        UserResponseDTO created = registration.registerUser(data);
        User user = account(created.getId());
        user.setRole(request.getRole());
        return response(repository.save(user));
    }

    @Transactional
    public AdminAccountResponse update(Long id, AdminAccountRequest request, String actor) {
        com.library.authservice.security.PasswordRules.validate(request.getPassword());
        User user = account(id);
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        // Evita perder el acceso de la cuenta que está administrando.
        if (user.getEmail().equalsIgnoreCase(actor) &&
                (request.getRole() != Role.ADMIN || !email.equalsIgnoreCase(actor)))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No puedes cambiar tu propio rol ni correo desde administración");
        if ("admin@library.cl".equalsIgnoreCase(user.getEmail()) &&
                (request.getRole() != Role.ADMIN || !email.equalsIgnoreCase(user.getEmail())))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La cuenta administradora principal debe conservar su rol y correo");
        if (!user.getEmail().equalsIgnoreCase(email) && repository.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");

        // Sincroniza por correo; los IDs de las dos bases son independientes.
        // Si el perfil falla, la transacción local no confirma la edición de Auth.
        restTemplate.put("http://user-service:8080/users/email/{email}",
                Map.of("name", request.getName().trim(), "email", email), user.getEmail());
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setRole(request.getRole());
        if (request.getPassword() != null) user.setPassword(encoder.encode(request.getPassword()));
        return response(repository.save(user));
    }

    @Transactional
    public void delete(Long id, String actor) {
        User user = account(id);
        if (user.getEmail().equalsIgnoreCase(actor) || "admin@library.cl".equalsIgnoreCase(user.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No puedes eliminar tu cuenta ni la administradora principal");
        // El perfil se elimina primero. Un fallo remoto no se presenta como éxito.
        restTemplate.delete("http://user-service:8080/users/email/{email}", user.getEmail());
        repository.delete(user);
    }
}
