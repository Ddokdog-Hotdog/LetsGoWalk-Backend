package com.ddokdoghotdog.gowalk.payment.dto;

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
public class ShopOrderItemDTO {

	private Long cartItemId;
	private Long productId;
	private String productName;
	private Long quantity;
}
