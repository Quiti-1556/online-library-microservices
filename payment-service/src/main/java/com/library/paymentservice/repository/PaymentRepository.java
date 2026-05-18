package com.library.paymentservice.repository;

import com.library.paymentservice.entity.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Long> {
}
