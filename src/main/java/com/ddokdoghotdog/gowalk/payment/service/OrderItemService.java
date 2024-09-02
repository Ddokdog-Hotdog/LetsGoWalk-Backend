package com.ddokdoghotdog.gowalk.payment.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddokdoghotdog.gowalk.entity.OrderItem;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.payment.repository.OrderItemsRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderItemService {

	@Autowired
	private OrderItemsRepository orderItemsRepository;
	
	// 멤버별 주문 내역 목록 조회
	public List<Map<String, Object>> findOrderItemsListByMemberId(Integer page, Long memberId){
		return orderItemsRepository.findOrderItemsListByMemberId(page, memberId);
	}
	
	// orderItemId 있는지 확인
	public OrderItem findByOrderItemId(Long orderItemId) {
		OrderItem orderItem = orderItemsRepository.findById(orderItemId)
				.orElseThrow(() -> new BusinessException(ErrorCode.ORDER_ITEM_NOT_FOUND));
		return orderItem;
	}
}
