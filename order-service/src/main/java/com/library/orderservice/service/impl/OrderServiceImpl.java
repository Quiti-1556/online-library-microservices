package com.library.orderservice.service.impl;

import com.library.orderservice.dto.OrderRequestDTO;
import com.library.orderservice.dto.OrderResponseDTO;
import com.library.orderservice.entity.OrderEntity;
import com.library.orderservice.repository.OrderRepository;
import com.library.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request) {

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
