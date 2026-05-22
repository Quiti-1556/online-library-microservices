package com.library.cartservice.service;

import com.library.cartservice.dto.CartRequestDTO;
import com.library.cartservice.dto.CartResponseDTO;
import com.library.cartservice.entity.Cart;

import java.util.List;

public interface CartService {

    CartResponseDTO addToCart(CartRequestDTO request);
    List<Cart> getAllCart();

    Cart getCartById(Long id);

    Cart updateCart(Long id, CartRequestDTO request);

    void deleteCart(Long id);
}
