package com.library.authservice.dto;

import com.library.authservice.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdminAccountRequest {
    @NotBlank @Size(min = 2, max = 100)
    private String name;
    @NotBlank @Email @Size(max = 100)
    private String email;
    // Al editar puede omitirse: no se envía ni se devuelve la contraseña existente.
    @Size(min = 6, max = 100)
    private String password;
    @NotNull
    private Role role;

    // Normaliza antes de @Valid para que espacios no oculten nombres demasiado cortos.
    public void setName(String value) { name = value == null ? null : value.trim(); }
    public void setEmail(String value) { email = value == null ? null : value.trim().toLowerCase(java.util.Locale.ROOT); }
}
