package com.prm393.footballfieldmanagement.repository;

import com.prm393.footballfieldmanagement.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
