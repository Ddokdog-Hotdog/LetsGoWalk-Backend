package com.ddokdoghotdog.gowalk.product.dto;

import org.springframework.web.multipart.MultipartFile;

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
public class ShopItemRegisterRequestDTO {

    private Long category;  // 카테고리 ID
    private String name;  // 상품명
    //private MultipartFile thumbnailImage;  // 상품 썸네일 이미지
    //private MultipartFile detailImage;  // 상품 설명 이미지
    private Long stockQuantity; // 재고
    private Long price;  // 가격
    private Long vendor;  // 판매업체 ID
}
