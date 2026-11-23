package com.library.userservice.dto;

import jakarta.validation.constraints.*;

// Actualiza identidad sin reemplazar el teléfono ni la dirección existentes.
public record IdentityUpdateDTO(
        @NotBlank @Size(min = 2, max = 100) String name,
        @NotBlank @Email @Size(max = 100) String email) {}
