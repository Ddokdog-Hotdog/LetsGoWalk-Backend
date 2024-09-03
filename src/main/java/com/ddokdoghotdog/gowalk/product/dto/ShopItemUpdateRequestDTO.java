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
public class ShopItemUpdateRequestDTO {

	private Long productId; // 상품 ID
    private Long category;  // 카테고리 ID
    private String name;  // 상품명
    private Long stockQuantity; // 재고
    private Long price;  // 가격
    private Long vendor;  // 판매업체 ID
    private Long isStockUpdate; // 재고 업데이트인지 여부
}
