package com.library.paymentservice.dto;

import lombok.Data;

@Data
public class PaymentResponseDTO {
    private Long id;

    private Long orderId;

    private Double amount;

    private String paymentMethod;

    private String status;
}
