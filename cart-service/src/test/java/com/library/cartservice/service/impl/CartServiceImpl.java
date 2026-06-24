package com.library.cartservice.service.impl;

import com.library.cartservice.dto.CartRequestDTO;
import com.library.cartservice.dto.CartResponseDTO;
import com.library.cartservice.entity.Cart;
import com.library.cartservice.exceptions.CartNotFoundException;
import com.library.cartservice.repository.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CartServiceImpl cartService;

    private CartRequestDTO buildRequestDTO() {
        CartRequestDTO dto = new CartRequestDTO();
        dto.setUserId(1L);
        dto.setBookId(10L);
        dto.setQuantity(2);
        return dto;
    }

    private Cart buildCart(Long id, Long userId, Long bookId, Integer quantity) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setUserId(userId);
        cart.setBookId(bookId);
        cart.setQuantity(quantity);
        return cart;
    }

    @Test
    @DisplayName("Debe agregar item al carrito correctamente")
    void shouldAddItemSuccessfully() {
        CartRequestDTO request = buildRequestDTO();
        Cart savedCart = buildCart(1L, 1L, 10L, 2);

        when(restTemplate.getForObject("http://localhost:8082/books/10", String.class))
                .thenReturn("{\"id\":10,\"title\":\"Libro test\"}");
        when(cartRepository.existsByUserIdAndBookId(1L, 10L)).thenReturn(false);
        when(cartRepository.save(any(Cart.class))).thenReturn(savedCart);

        CartResponseDTO response = cartService.addItem(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals(10L, response.getBookId());
        assertEquals(2, response.getQuantity());

        verify(restTemplate, times(1))
                .getForObject("http://localhost:8082/books/10", String.class);
        verify(cartRepository, times(1)).existsByUserIdAndBookId(1L, 10L);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción si el libro ya está en el carrito del usuario")
    void shouldThrowExceptionWhenBookAlreadyExistsInCart() {
        CartRequestDTO request = buildRequestDTO();

        when(restTemplate.getForObject("http://localhost:8082/books/10", String.class))
                .thenReturn("{\"id\":10,\"title\":\"Libro test\"}");
        when(cartRepository.existsByUserIdAndBookId(1L, 10L)).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartService.addItem(request)
        );

        assertEquals("El libro ya está en el carrito de este usuario", exception.getMessage());

        verify(restTemplate, times(1))
                .getForObject("http://localhost:8082/books/10", String.class);
        verify(cartRepository, times(1)).existsByUserIdAndBookId(1L, 10L);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("Debe listar items del carrito por usuario")
    void shouldReturnItemsByUser() {
        Cart c1 = buildCart(1L, 1L, 10L, 2);
        Cart c2 = buildCart(2L, 1L, 11L, 1);

        when(cartRepository.findByUserId(1L)).thenReturn(List.of(c1, c2));

        List<CartResponseDTO> response = cartService.getItemsByUser(1L);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(10L, response.get(0).getBookId());
        assertEquals(11L, response.get(1).getBookId());

        verify(cartRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("Debe retornar lista vacía si el usuario no tiene items en el carrito")
    void shouldReturnEmptyListWhenUserHasNoCartItems() {
        when(cartRepository.findByUserId(99L)).thenReturn(List.of());

        List<CartResponseDTO> response = cartService.getItemsByUser(99L);

        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(cartRepository, times(1)).findByUserId(99L);
    }

    @Test
    @DisplayName("Debe eliminar item del carrito correctamente")
    void shouldDeleteCartSuccessfully() {
        Cart cart = buildCart(1L, 1L, 10L, 2);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(cart));
        doNothing().when(cartRepository).delete(cart);

        assertDoesNotThrow(() -> cartService.deleteCart(1L));

        verify(cartRepository, times(1)).findById(1L);
        verify(cartRepository, times(1)).delete(cart);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar item inexistente del carrito")
    void shouldThrowExceptionWhenDeletingNonExistingCartItem() {
        when(cartRepository.findById(99L)).thenReturn(Optional.empty());

        CartNotFoundException exception = assertThrows(
                CartNotFoundException.class,
                () -> cartService.deleteCart(99L)
        );

        assertEquals("Item de carrito no encontrado con id 99", exception.getMessage());
        verify(cartRepository, times(1)).findById(99L);
        verify(cartRepository, never()).delete(any(Cart.class));
    }
}