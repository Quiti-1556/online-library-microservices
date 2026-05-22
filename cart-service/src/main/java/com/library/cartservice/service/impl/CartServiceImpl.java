package com.library.cartservice.service.impl;

import com.library.cartservice.dto.CartRequestDTO;
import com.library.cartservice.dto.CartResponseDTO;
import com.library.cartservice.entity.Cart;
import com.library.cartservice.repository.CartRepository;
import com.library.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Override
    public CartResponseDTO addToCart(CartRequestDTO request) {

        Cart cart = new Cart();

        cart.setUserId(request.getUserId());
        cart.setBookId(request.getBookId());
        cart.setQuantity(request.getQuantity());

        Cart savedCart = cartRepository.save(cart);

        CartResponseDTO response = new CartResponseDTO();

        response.setId(savedCart.getId());
        response.setUserId(savedCart.getUserId());
        response.setBookId(savedCart.getBookId());
        response.setQuantity(savedCart.getQuantity());

        return response;
    }
    @Override
    public List<Cart> getAllCart() {

        return cartRepository.findAll();
    }

    @Override
    public Cart getCartById(Long id) {

        return cartRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Registro no encontrado")
                );
    }

    @Override
    public Cart updateCart(Long id, CartRequestDTO request) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Registro no encontrado")
                );
        return cart;
    }

    @Override
    public void deleteCart(Long id) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Registro no encontrado")
                );
    }
}
