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

import com.ddokdoghotdog.gowalk.global.annotation.RequiredMemberId;
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
	@RequiredMemberId
	@GetMapping("")
	public ResponseEntity<?> getShopItemList(@RequestParam("page") Integer page, Long memberId){
		Map<String, Object> shopList = productService.getProductList(page, memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(shopList);
	}
	
	// 쇼핑몰 메인화면 (베스트 상품)
	@RequiredMemberId
	@GetMapping("best")
	public ResponseEntity<?> getShopBestItemList(Long memberId){
		Map<String, Object> shopList = productService.getBestProductList(memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(shopList);
	}
	
	// 찜 목록 화면
	@RequiredMemberId
	@GetMapping("like")
	public ResponseEntity<?> getShopItemLikeList(@RequestParam("page") Integer page, Long memberId){
		Map<String, Object> shopList = productService.getProductLikeList(page, memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(shopList);
	}
	
	// 쇼핑몰 상세화면
	@RequiredMemberId
	@GetMapping("detail")
	public ResponseEntity<?> getShopItemDetail(@RequestParam("productId") Long productId, Long memberId){
		Map<String, Object> itemDetail = productService.getProductDetail(productId, memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(itemDetail);
	}
	
	// 특정 상품에 "찜" 추가
	@RequiredMemberId
	@PostMapping("like/register")
	public ResponseEntity<?> insertProductLike(@RequestParam("productId") Long productId, Long memberId){
		productService.insertProductLike(productId, memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(productId + "번 상품 찜 추가 성공");
	}
	
	
	// 특정 상품에 "찜" 해제
	@RequiredMemberId
	@DeleteMapping("like/register")
	public ResponseEntity<?> deleteProductLike(@RequestParam("productId") Long productId, Long memberId){
		productService.deleteProductLike(productId, memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(productId + "번 상품 찜 해제 성공");
	}
	
	// 카테고리 등록
	@PostMapping("category/insert")
	public ResponseEntity<?> insertCategory(@RequestParam("category") String category){
		productService.insertCategory(category);
		return ResponseEntity.status(HttpStatus.OK)
				.body("카테고리 등록 성공");
	}
	
	// 카테고리 목록 가져오기
	@GetMapping("category/list")
	public ResponseEntity<?> findCategoryAll(){
		return ResponseEntity.status(HttpStatus.OK)
				.body(productService.findCategoryAll());
	}
	
	// 카테고리 삭제
	@DeleteMapping("category/delete")
	public ResponseEntity<?> deleteCategory(@RequestParam("categoryId") Long categoryId){
		productService.deleteCategory(categoryId);
		return ResponseEntity.status(HttpStatus.OK)
				.body("카테고리 삭제 성공");
	}
	
	// 판매업체 등록
	@PostMapping("vendor/insert")
	public ResponseEntity<?> insertVendor(@RequestParam("vendor") String vendor){
		productService.insertVendor(vendor);
		return ResponseEntity.status(HttpStatus.OK)
				.body("판매업체 등록 성공");
	}
	
	// 판매업체 목록 가져오기
	@GetMapping("vendor/list")
	public ResponseEntity<?> findVendorAll(){
		return ResponseEntity.status(HttpStatus.OK)
				.body(productService.findVendorAll());
	}
	
	// 판매업체 삭제
	@DeleteMapping("vendor/delete")
	public ResponseEntity<?> vendorCategory(@RequestParam("vendorId") Long vendorId){
		productService.deleteVendor(vendorId);
		return ResponseEntity.status(HttpStatus.OK)
				.body("판매업체 삭제 성공");
	}
}
