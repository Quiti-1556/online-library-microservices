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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger =
            LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        logger.info("Creando orden");

        OrderEntity order = new OrderEntity();

        order.setUserId(request.getUserId());
        order.setTotal(request.getTotal());
        order.setStatus(request.getStatus());

        OrderEntity savedOrder = orderRepository.save(order);

        OrderResponseDTO response = new OrderResponseDTO();

        response.setId(savedOrder.getId());
        response.setUserId(savedOrder.getUserId());
        response.setTotal(savedOrder.getTotal());
        response.setStatus(savedOrder.getStatus());

        return response;
    }
}
