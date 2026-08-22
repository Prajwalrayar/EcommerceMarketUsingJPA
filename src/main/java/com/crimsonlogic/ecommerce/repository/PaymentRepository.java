package com.crimsonlogic.ecommerce.repository;

import com.crimsonlogic.ecommerce.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}