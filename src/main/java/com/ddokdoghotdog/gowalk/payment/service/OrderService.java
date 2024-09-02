package com.ddokdoghotdog.gowalk.payment.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Order;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.payment.repository.OrdersRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderService {

	@Autowired
	private OrdersRepository ordersRepository;
	
	// 한달간 상품별 매출
	public List<Map<String, Object>> findMonthStatisticsInfo(){
		return ordersRepository.findMonthStatisticsInfo();
	}
	
	// 주문건별 멤버 정보 및 상품 정보
	public List<Map<String, Object>> findAllOrderList(Integer page){
		return ordersRepository.findAllOrderList(page);
	}
	
	// Orderid로 주문 정보 가져오기
	@Transactional
	public Order findById(Long orderId) {
		Order order = ordersRepository.findById(orderId)
				.orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
		return order;
	}
}
