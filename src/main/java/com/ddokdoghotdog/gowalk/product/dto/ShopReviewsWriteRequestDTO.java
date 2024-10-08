package com.ddokdoghotdog.gowalk.product.dto;

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
public class ShopReviewsWriteRequestDTO {

	private Long productId;
	private String title;
	private String contents;
	
}
