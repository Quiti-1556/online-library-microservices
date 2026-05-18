package com.library.paymentservice.controller;

import com.library.paymentservice.dto.PaymentRequestDTO;
import com.library.paymentservice.dto.PaymentResponseDTO;
import com.library.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponseDTO createPayment(@RequestBody PaymentRequestDTO request) {

        return paymentService.createPayment(request);
    }
}
