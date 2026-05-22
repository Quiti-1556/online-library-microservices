package com.library.cartservice.controller;

import com.library.cartservice.dto.CartRequestDTO;
import com.library.cartservice.dto.CartResponseDTO;
import com.library.cartservice.entity.Cart;
import com.library.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    public CartResponseDTO addToCart(@RequestBody CartRequestDTO request) {

        return cartService.addToCart(request);
    }
    @GetMapping
    public String getBooks() {

        return "Cart Service funcionando correctamente";
    }
    @GetMapping("/error")
    public String error() {

        throw new RuntimeException("Error de prueba");
    }
    @GetMapping
    public List<Cart> getAllCarts() {

        return cartService.getAllCart();
    }

    @GetMapping("/{id}")
    public Cart getCartById(@PathVariable Long id) {

        return cartService.getCartById(id);
    }

    @PutMapping("/{id}")
    public Cart updateBook(@PathVariable Long id,
                           @RequestBody CartRequestDTO request) {

        return cartService.updateCart(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {

        cartService.deleteCart(id);

        return "Eliminado correctamente";
    }
}
