package com.library.orderservice.dto;

import lombok.Data;

@Data
public class OrderResponseDTO {
    private Long id;

    private Long userId;

    private Double total;

    private String status;
}
