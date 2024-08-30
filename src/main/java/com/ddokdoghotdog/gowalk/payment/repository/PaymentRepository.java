package com.ddokdoghotdog.gowalk.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
