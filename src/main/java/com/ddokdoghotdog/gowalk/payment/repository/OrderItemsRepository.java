package com.ddokdoghotdog.gowalk.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddokdoghotdog.gowalk.entity.OrderItem;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long>{

}
