package com.library.authservice.service.impl;

import com.library.authservice.dto.LoginRequestDTO;
import com.library.authservice.dto.LoginResponseDTO;
import com.library.authservice.dto.RegisterRequestDTO;
import com.library.authservice.dto.UserResponseDTO;
import com.library.authservice.entity.Role;
import com.library.authservice.entity.User;
import com.library.authservice.repository.UserRepository;
import com.library.authservice.security.JwtService;
import com.library.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public UserResponseDTO registerUser(RegisterRequestDTO request) {
        com.library.authservice.security.PasswordRules.validate(request.getPassword());
        logger.info("Creando nuevo usuario con email {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("Intento de registro con correo duplicado: {}", request.getEmail());
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "El correo ya está registrado");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CLIENT);

        User savedUser = userRepository.save(user);

        java.util.Map<String, Object> userRequest = new java.util.HashMap<>();
        userRequest.put("name", request.getName());
        userRequest.put("email", request.getEmail());
        // PUT permite reintentar después de un timeout que haya dejado solo el perfil.
        // Si ya existe, conserva teléfono/dirección; si falta, perfiles lo crea.
        restTemplate.put("http://user-service:8080/users/email/{email}", userRequest, request.getEmail());

        UserResponseDTO response = new UserResponseDTO();
        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());

        logger.info("Usuario registrado correctamente con id {}", savedUser.getId());

        return response;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        logger.info("Intentando login para email {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logger.warn("Usuario no encontrado para email {}", request.getEmail());
                    return new org.springframework.web.server.ResponseStatusException(
                            org.springframework.http.HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Contraseña incorrecta para email {}", request.getEmail());
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos");
        }

        String token = jwtService.generateToken(user);

        logger.info("Login exitoso para email {}", request.getEmail());

        return new LoginResponseDTO(
                "Inicio de sesión exitoso",
                user.getEmail(),
                user.getRole().name(),
                token
        );
    }
}
