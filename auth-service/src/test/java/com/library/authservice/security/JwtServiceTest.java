package com.library.authservice.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    @DisplayName("Debe generar token y extraer email correctamente")
    void shouldGenerateAndExtractEmail() {
        JwtService jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey",
                "mysecretkeymysecretkeymysecretkey123456");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);

        jwtService.init();

        String token = jwtService.generateToken("juan@email.com");

        assertNotNull(token);
        assertFalse(token.isBlank());

        String email = jwtService.extractEmail(token);

        assertEquals("juan@email.com", email);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el token es inválido")
    void shouldThrowExceptionWhenTokenIsInvalid() {
        JwtService jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey",
                "mysecretkeymysecretkeymysecretkey123456");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);

        jwtService.init();

        assertThrows(Exception.class, () -> jwtService.extractEmail("token-invalido"));
    }
}