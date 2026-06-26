package com.library.orderservice.service.impl;

import com.library.orderservice.dto.OrderRequestDTO;
import com.library.orderservice.dto.OrderResponseDTO;
import com.library.orderservice.entity.OrderEntity;
import com.library.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderServiceImpl orderService;

    private OrderRequestDTO buildRequestDTO() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setUserId(1L);
        dto.setBookId(10L);
        dto.setTotal(24990.0);
        dto.setStatus("CREATED");
        return dto;
    }

    private OrderEntity buildOrderEntity(Long id, Long userId, Double total, String status) {
        OrderEntity order = new OrderEntity();
        order.setId(id);
        order.setUserId(userId);
        order.setTotal(total);
        order.setStatus(status);
        return order;
    }

    @Test
    @DisplayName("Debe crear una orden correctamente y consumir servicios externos")
    void shouldCreateOrderSuccessfully() {
        OrderRequestDTO request = buildRequestDTO();
        OrderEntity savedOrder = buildOrderEntity(1L, 1L, 24990.0, "CREATED");

        when(restTemplate.getForObject("http://localhost:8089/users/1", String.class))
                .thenReturn("{\"id\":1,\"name\":\"Usuario Test\"}");
        when(restTemplate.getForObject("http://localhost:8082/books/10", String.class))
                .thenReturn("{\"id\":10,\"title\":\"Libro Test\"}");
        when(restTemplate.getForObject("http://localhost:8086/inventory/book/10", String.class))
                .thenReturn("{\"bookId\":10,\"stock\":5}");

        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedOrder);

        when(restTemplate.postForObject(
                eq("http://localhost:8088/payments"),
                any(Map.class),
                eq(String.class)
        )).thenReturn("{\"status\":\"PAID\"}");

        when(restTemplate.postForEntity(
                eq("http://localhost:9090/notifications"),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(ResponseEntity.ok("{\"status\":\"NOTIFIED\"}"));

        OrderResponseDTO response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getUserId());
        assertEquals(24990.0, response.getTotal());
        assertEquals("CREATED", response.getStatus());

        verify(restTemplate, times(1))
                .getForObject("http://localhost:8089/users/1", String.class);
        verify(restTemplate, times(1))
                .getForObject("http://localhost:8082/books/10", String.class);
        verify(restTemplate, times(1))
                .getForObject("http://localhost:8086/inventory/book/10", String.class);

        verify(orderRepository, times(1)).save(any(OrderEntity.class));

        verify(restTemplate, times(1))
                .postForObject(eq("http://localhost:8088/payments"), any(Map.class), eq(String.class));

        verify(restTemplate, times(1))
                .postForEntity(eq("http://localhost:9090/notifications"), any(HttpEntity.class), eq(String.class));
    }

    @Test
    @DisplayName("Debe construir correctamente el payload del pago")
    void shouldBuildPaymentPayloadCorrectly() {
        OrderRequestDTO request = buildRequestDTO();
        OrderEntity savedOrder = buildOrderEntity(5L, 1L, 24990.0, "CREATED");

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("{}");
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedOrder);
        when(restTemplate.postForObject(
                eq("http://localhost:8088/payments"),
                any(Map.class),
                eq(String.class)
        )).thenReturn("{\"status\":\"PAID\"}");
        when(restTemplate.postForEntity(
                eq("http://localhost:9090/notifications"),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(ResponseEntity.ok("{\"status\":\"NOTIFIED\"}"));

        orderService.createOrder(request);

        ArgumentCaptor<Map<String, Object>> paymentCaptor = ArgumentCaptor.forClass(Map.class);

        verify(restTemplate).postForObject(
                eq("http://localhost:8088/payments"),
                paymentCaptor.capture(),
                eq(String.class)
        );

        Map<String, Object> paymentPayload = paymentCaptor.getValue();

        assertEquals(5L, paymentPayload.get("orderId"));
        assertEquals(24990.0, paymentPayload.get("amount"));
        assertEquals("CARD", paymentPayload.get("paymentMethod"));
        assertEquals("PAID", paymentPayload.get("status"));
    }

    @Test
    @DisplayName("Debe construir correctamente el payload de la notificación")
    void shouldBuildNotificationPayloadCorrectly() {
        OrderRequestDTO request = buildRequestDTO();
        OrderEntity savedOrder = buildOrderEntity(7L, 1L, 24990.0, "CREATED");

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("{}");
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedOrder);
        when(restTemplate.postForObject(
                eq("http://localhost:8088/payments"),
                any(Map.class),
                eq(String.class)
        )).thenReturn("{\"status\":\"PAID\"}");
        when(restTemplate.postForEntity(
                eq("http://localhost:9090/notifications"),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(ResponseEntity.ok("{\"status\":\"NOTIFIED\"}"));

        orderService.createOrder(request);

        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);

        verify(restTemplate).postForEntity(
                eq("http://localhost:9090/notifications"),
                entityCaptor.capture(),
                eq(String.class)
        );

        HttpEntity capturedEntity = entityCaptor.getValue();
        Object body = capturedEntity.getBody();

        assertNotNull(body);
        assertTrue(body instanceof Map);

        Map<String, Object> notificationPayload = (Map<String, Object>) body;

        assertEquals(1L, notificationPayload.get("userId"));
        assertEquals("ORDER_CREATED", notificationPayload.get("type"));
        assertEquals("Tu orden 7 fue creada y pagada correctamente", notificationPayload.get("message"));
    }
}
