package com.library.cartservice.service.impl;

import com.library.cartservice.dto.CartRequestDTO;
import com.library.cartservice.dto.CartResponseDTO;
import com.library.cartservice.entity.Cart;
import com.library.cartservice.repository.CartRepository;
import com.library.cartservice.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    private static final Logger logger =
            LoggerFactory.getLogger(CartServiceImpl.class);

    @Override
    public CartResponseDTO addToCart(CartRequestDTO request) {
        logger.info("Creando Nueva carta");

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
        logger.info("Actualizando regitro con ID: {}", id);

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Registro no encontrado")
                );
        return cart;
    }

    @Override
    public void deleteCart(Long id) {
        logger.info("Eliminando registro con ID: {}", id);
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Registro no encontrado")
                );
    }
}
