package com.library.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class AuthSessionController {
    @GetMapping("/auth/session")
    @Operation(summary = "Consultar identidad y rol vigentes de la sesión")
    public Map<String, String> session(Authentication user) {
        // El filtro JWT obtiene este rol de la base de datos, no del token antiguo.
        String role = user.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        return Map.of("email", user.getName(), "role", role);
    }
}
