package com.library.authservice.security;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;

class PasswordRulesTest {
    @Test void limitaBytesParaEvitarRechazoOTruncamientoDeBcrypt() {
        assertDoesNotThrow(() -> PasswordRules.validate("a".repeat(72)));
        assertDoesNotThrow(() -> PasswordRules.validate("é".repeat(36)));
        assertDoesNotThrow(() -> PasswordRules.validate(null));
        assertEquals(400, assertThrows(ResponseStatusException.class,
                () -> PasswordRules.validate("é".repeat(37))).getStatusCode().value());
        assertThrows(ResponseStatusException.class, () -> PasswordRules.validate("a".repeat(73)));
    }
}
