package com.library.bookservice;

import com.library.bookservice.dto.BookRequestDTO;
import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookValidationTest {
    @Test void precioYStockSonObligatoriosTambienEnBackend() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            BookRequestDTO book = new BookRequestDTO();
            book.setTitle("Libro válido"); book.setAuthor("Autora válida");
            var violations = factory.getValidator().validate(book);
            assertEquals(2, violations.size());
            book.setPrice(12.5); book.setStock(0);
            assertTrue(factory.getValidator().validate(book).isEmpty());
            book.setStock(-1);
            assertFalse(factory.getValidator().validate(book).isEmpty());
        }
    }
}
