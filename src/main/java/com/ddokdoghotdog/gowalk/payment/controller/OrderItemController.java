package com.ddokdoghotdog.gowalk.payment.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.global.annotation.RequiredMemberId;
import com.ddokdoghotdog.gowalk.payment.service.OrderItemService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderItemController {

	@Autowired
	private OrderItemService orderItemService;
	
	// 멤버별 주문 상품 목록 조회
	@RequiredMemberId
	@GetMapping("/api/shop/orderitem")
	public ResponseEntity<?> findOrderItemsListByMemberId(@RequestParam("page") Integer page, Long memberId){
		List<Map<String, Object>> orderItemList = orderItemService.findOrderItemsListByMemberId(page, memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(orderItemList);
	}
}
