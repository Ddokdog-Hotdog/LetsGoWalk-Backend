package com.ddokdoghotdog.gowalk.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Order;
import com.ddokdoghotdog.gowalk.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

	// 주문id로 결제정보 가져오기
	Optional<Payment> findByOrder(Order order);
}
