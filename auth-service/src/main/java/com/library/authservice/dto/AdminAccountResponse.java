package com.library.authservice.dto;

import com.library.authservice.entity.Role;

// ID de autenticación, nunca ID de perfil; no contiene hash ni contraseña.
public record AdminAccountResponse(Long id, String name, String email, Role role) {}
