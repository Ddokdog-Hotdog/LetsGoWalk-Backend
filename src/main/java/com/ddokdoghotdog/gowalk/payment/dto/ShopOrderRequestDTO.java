package com.ddokdoghotdog.gowalk.payment.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ShopOrderRequestDTO {

	private List<ShopOrderItemDTO> orderItems; // 주문 상품 배열
	private Integer totalAmount; // 상품 총액
	private String address; // 주소
	private Long point; // 사용 포인트
	
}
