package com.ddokdoghotdog.gowalk.cart.dto;

import com.ddokdoghotdog.gowalk.product.dto.ShopItemDeleteDTO;

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
public class ShopCartsRequestDTO {

	private Long productId;
	private Long quantity;
}
