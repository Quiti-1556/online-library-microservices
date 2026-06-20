package com.library.cartservice.controller;

import com.library.cartservice.dto.CartRequestDTO;
import com.library.cartservice.dto.CartResponseDTO;
import com.library.cartservice.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;

    @GetMapping("/health")
    public String health() {
        return "cart service funcionando correctamente";
    }
    @PostMapping
    public ResponseEntity<CartResponseDTO> addItem(@Valid @RequestBody CartRequestDTO request) {
        log.info("Solicitud para agregar item al carrito, userId {}", request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartResponseDTO>> getItemsByUser(@PathVariable Long userId) {
        log.info("Consultando carrito del usuario {}", userId);
        return ResponseEntity.ok(cartService.getItemsByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        log.info("Eliminando item del carrito con id {}", id);
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
