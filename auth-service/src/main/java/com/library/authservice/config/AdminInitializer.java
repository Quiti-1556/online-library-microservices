package com.library.authservice.config;

import com.library.authservice.entity.Role;
import com.library.authservice.entity.User;
import com.library.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    @org.springframework.beans.factory.annotation.Value("${ADMIN_INITIAL_PASSWORD:}")
    private String initialPassword;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@library.cl";

        if (!userRepository.existsByEmail(adminEmail)) {
            // Nunca crear una cuenta privilegiada con una contraseña pública de ejemplo.
            if (initialPassword == null || initialPassword.length() < 12) {
                throw new IllegalStateException("Configura ADMIN_INITIAL_PASSWORD con al menos 12 caracteres para la cuenta inicial");
            }
            com.library.authservice.security.PasswordRules.validate(initialPassword);
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(initialPassword));
            admin.setRole(Role.ADMIN);

            userRepository.save(admin);
        }
    }
}
