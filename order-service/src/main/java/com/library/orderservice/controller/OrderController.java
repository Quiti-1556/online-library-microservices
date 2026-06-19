package com.library.orderservice.controller;

import com.library.orderservice.dto.OrderRequestDTO;
import com.library.orderservice.dto.OrderResponseDTO;
import com.library.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        log.info("Creando orden para userId {} y bookId {}", request.getUserId(), request.getBookId());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }
}
