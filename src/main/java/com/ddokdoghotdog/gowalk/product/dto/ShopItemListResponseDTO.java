package com.ddokdoghotdog.gowalk.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopItemListResponseDTO {

	private Long productId; // 상품id
	private String name;  // 상품명
	private Long price;  // 가격
	private String thumbnailImage;  // 상품 썸네일 이미지
	private Long stockQuantity; // 재고
	private String isLike; // 사용자의 좋아요 여부
    private String category;  // 카테고리 ID
    private String vendor;  // 판매업체 ID
}
