package com.ddokdoghotdog.gowalk.payment.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.payment.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("api/admin/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	// 월별 통계
	@GetMapping("month")
	public ResponseEntity<?> findMonthStatisticsInfo(){
		List<Map<String, Object>> monthStaticsInfo = orderService.findMonthStatisticsInfo();
		return ResponseEntity.status(HttpStatus.OK)
				.body(monthStaticsInfo);
	}

	// 주문건별 멤버 정보 및 상품 정보
	@GetMapping("")
	public ResponseEntity<?> findAllOrderList(@RequestParam("page") Integer page){
		List<Map<String, Object>> orderList = orderService.findAllOrderList(page);
		return ResponseEntity.status(HttpStatus.OK)
				.body(orderList);
	}
}
