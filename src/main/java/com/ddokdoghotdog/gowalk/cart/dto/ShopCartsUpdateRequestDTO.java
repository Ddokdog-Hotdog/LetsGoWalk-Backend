package com.ddokdoghotdog.gowalk.cart.dto;

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
public class ShopCartsUpdateRequestDTO {

	private Long cartItemId;
	private Long productId;
	private Long quantity;
}
