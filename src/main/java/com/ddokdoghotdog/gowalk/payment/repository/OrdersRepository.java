package com.ddokdoghotdog.gowalk.payment.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.ddokdoghotdog.gowalk.entity.Order;

public interface OrdersRepository extends JpaRepository<Order, Long>{

	Optional<Order> findByKakaoOrderId(String kakaoOrderId);
}
