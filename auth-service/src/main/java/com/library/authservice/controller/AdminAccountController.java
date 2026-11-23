package com.library.authservice.controller;

import com.library.authservice.dto.*;
import com.library.authservice.service.AdminAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
@Tag(name = "Administración de cuentas", description = "CRUD exclusivo ADMIN con identificadores de Auth")
public class AdminAccountController {
    private final AdminAccountService accounts;

    @GetMapping @Operation(summary = "Listar cuentas y roles sin contraseñas")
    public List<AdminAccountResponse> list() { return accounts.list(); }

    @GetMapping("/{id}") @Operation(summary = "Consultar una cuenta por ID de Auth")
    public AdminAccountResponse get(@PathVariable Long id) { return accounts.get(id); }

    @PostMapping @Operation(summary = "Crear cuenta y perfil con rol ADMIN o CLIENT")
    public ResponseEntity<AdminAccountResponse> create(@Valid @RequestBody AdminAccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accounts.create(request));
    }

    @PutMapping("/{id}") @Operation(summary = "Editar cuenta y sincronizar identidad del perfil")
    public AdminAccountResponse update(@PathVariable Long id, @Valid @RequestBody AdminAccountRequest request, Principal actor) {
        return accounts.update(id, request, actor.getName());
    }

    @DeleteMapping("/{id}") @Operation(summary = "Eliminar cuenta y perfil sin confundir sus identificadores")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal actor) {
        accounts.delete(id, actor.getName());
        return ResponseEntity.noContent().build();
    }
}
