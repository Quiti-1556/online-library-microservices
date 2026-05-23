package com.library.paymentservice.service.impl;

import com.library.paymentservice.dto.PaymentRequestDTO;
import com.library.paymentservice.dto.PaymentResponseDTO;
import com.library.paymentservice.entity.Payment;
import com.library.paymentservice.repository.PaymentRepository;
import com.library.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PaymentServiceImp implements PaymentService {
    private static final Logger logger =
            LoggerFactory.getLogger(PaymentServiceImp.class);

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO request) {
        logger.info("se pago");

        Payment payment = new Payment();

        payment.setOrderId(request.getOrderId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus(request.getStatus());

        Payment savedPayment = paymentRepository.save(payment);

        PaymentResponseDTO response = new PaymentResponseDTO();

        response.setId(savedPayment.getId());
        response.setOrderId(savedPayment.getOrderId());
        response.setAmount(savedPayment.getAmount());
        response.setPaymentMethod(savedPayment.getPaymentMethod());
        response.setStatus(savedPayment.getStatus());

        return response;
    }

}
