package com.library.paymentservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private Double amount;

    private String paymentMethod;

    private String status;

}
