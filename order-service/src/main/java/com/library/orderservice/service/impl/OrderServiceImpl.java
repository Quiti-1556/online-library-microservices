package com.library.orderservice.service.impl;

import com.library.orderservice.dto.OrderRequestDTO;
import com.library.orderservice.dto.OrderResponseDTO;
import com.library.orderservice.entity.OrderEntity;
import com.library.orderservice.repository.OrderRepository;
import com.library.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger =
            LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    private OrderResponseDTO mapToResponse(OrderEntity order) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(order.getId());
        response.setUserId(order.getUserId());
        response.setTotal(order.getTotal());
        response.setStatus(order.getStatus());
        return response;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        logger.info("Creando orden para userId {}", request.getUserId());

        String responseBook = restTemplate.getForObject(
                "http://localhost:8082/books/" + request.getBookId(),
                String.class
        );

        logger.info("Respuesta desde book-service: {}", responseBook);

        OrderEntity order = new OrderEntity();
        order.setUserId(request.getUserId());
        order.setTotal(request.getTotal());
        order.setStatus(request.getStatus());

        OrderEntity savedOrder = orderRepository.save(order);

        logger.info("Orden creada correctamente con id {}", savedOrder.getId());

        return mapToResponse(savedOrder);
    }
}
