package com.library.authservice.security;

import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public final class PasswordRules {
    private PasswordRules() {}

    // BCrypt limita bytes, no caracteres: una letra acentuada puede ocupar más de uno.
    public static void validate(String password) {
        if (password != null && password.getBytes(StandardCharsets.UTF_8).length > 72)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña supera el máximo de 72 bytes UTF-8 permitido por BCrypt");
    }
}
