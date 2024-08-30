package com.ddokdoghotdog.gowalk.product.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ddokdoghotdog.gowalk.product.dto.ShopItemDeleteDTO;
import com.ddokdoghotdog.gowalk.product.dto.ShopItemRegisterRequestDTO;
import com.ddokdoghotdog.gowalk.product.dto.ShopItemUpdateRequestDTO;
import com.ddokdoghotdog.gowalk.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("api/shop/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	// 상품 등록
	@PostMapping("register")
	public ResponseEntity<?> productSave(
			@RequestPart("shopItemRegisterRequestDTO") ShopItemRegisterRequestDTO shopItemRegisterRequestDTO,
			@RequestPart("thumbnailImage") MultipartFile thumbnailImage,
			@RequestPart("detailImage") MultipartFile detailImage) {
		log.info("컨트롤러단 상품 등록 정보 : {}", shopItemRegisterRequestDTO.toString());
		log.info("썸네일 이미지 정보 : {}", thumbnailImage.toString());
		productService.productSave(shopItemRegisterRequestDTO, thumbnailImage, detailImage);
		return ResponseEntity.status(HttpStatus.OK).body("상품 등록 성공");
	}

	// 상품 수정
	@PutMapping("update")
	public ResponseEntity<?> productSave(
			@RequestPart("shopItemUpdateRequestDTO") ShopItemUpdateRequestDTO shopItemUpdateRequestDTO,
			@RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage,
			@RequestPart(value = "detailImage",  required = false) MultipartFile detailImage) {
		
		productService.productUpdate(shopItemUpdateRequestDTO, thumbnailImage, detailImage);
		return ResponseEntity.status(HttpStatus.OK).body("상품 수정 성공");
	}
	
	// 상품 삭제
	@DeleteMapping("delete")
	public ResponseEntity<?> productDelte(@RequestBody ShopItemDeleteDTO shopItemDeleteDTO) {
		productService.productDelete(shopItemDeleteDTO);
		return ResponseEntity.status(HttpStatus.OK).body("상품 삭제 성공");
	}
	
	
	// 쇼핑몰 메인화면
	@GetMapping("")
	public ResponseEntity<?> getShopItemList(@RequestParam("page") Integer page){
		log.info("현재 페이지 : {}", page);
		// 사용자의 id 가져오는 작업 필요함
		long memberId = 2;
		Map<String, Object> shopList = productService.getProductList(page, memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(shopList);
	}
	
	// 찜 목록 화면
	@GetMapping("like")
	public ResponseEntity<?> getShopItemLikeList(@RequestParam("page") Integer page){
		log.info("현재 페이지 : {}", page);
		// 사용자의 id 가져오는 작업 필요함
		long memberId = 2;
		Map<String, Object> shopList = productService.getProductLikeList(page, memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(shopList);
	}
	
	// 쇼핑몰 상세화면
	@GetMapping("detail")
	public ResponseEntity<?> getShopItemDetail(@RequestParam("productId") Long productId){
		// 사용자의 id 가져오는 작업 필요함
		long memberId = 2;
		Map<String, Object> itemDetail = productService.getProductDetail(productId, memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(itemDetail);
	}
	
	// 특정 상품에 "찜" 추가
	@PostMapping("like/register")
	public ResponseEntity<?> insertProductLike(@RequestParam("productId") Long productId){
		// 사용자의 id 가져오는 작업 필요함
		long memberId = 2;
		productService.insertProductLike(productId, memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(productId + "번 상품 찜 추가 성공");
	}
	
	
	// 특정 상품에 "찜" 해제
	@DeleteMapping("like/register")
	public ResponseEntity<?> deleteProductLike(@RequestParam("productId") Long productId){
		// 사용자의 id 가져오는 작업 필요함
		long memberId = 2;
		productService.deleteProductLike(productId, memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(productId + "번 상품 찜 해제 성공");
	}

}
