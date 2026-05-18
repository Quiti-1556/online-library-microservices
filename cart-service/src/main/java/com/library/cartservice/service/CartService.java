package com.library.cartservice.service;

import com.library.cartservice.dto.CartRequestDTO;
import com.library.cartservice.dto.CartResponseDTO;

public interface CartService {

    CartResponseDTO addToCart(CartRequestDTO request);
}
