package com.ddokdoghotdog.gowalk.payment.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderItemDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderRequestDTO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentParentService {

	@Autowired
	private PaymentSychService paymentSychService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Transactional
	public void processProduct(ShopOrderRequestDTO shopOrderRequestDTO, Long memberId, 
			String orderId, String tid) {
		
		// 주문 테이블 및 결제 테이블에 insert
		// 포인트 갱신 및 장바구니에서 주문 상품 제거
		paymentService.insertOrderAndInsertPayment(shopOrderRequestDTO, memberId, orderId, tid);
				
		// productId를 기준으로 오름차순 정렬
		List<ShopOrderItemDTO> orderItemList = shopOrderRequestDTO.getOrderItems();
		orderItemList.sort(Comparator.comparing(ShopOrderItemDTO::getProductId));
		
		// 주문 상품별로 재고update 및 주문테이블에 insert
		for(ShopOrderItemDTO orderItem : orderItemList) {
			paymentService.updateStockAndInsertOrderItem(orderItem, orderId);
			// paymentSychService.processProduct(orderItem, memberId, orderId, tid);
			
		}
		
	}
}
