package com.library.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class UserResponseDTO {

    @NotNull
    int id;
    @NotBlank
    String name;
    @NotBlank
    String email;
    @NotBlank
    String role;
}
