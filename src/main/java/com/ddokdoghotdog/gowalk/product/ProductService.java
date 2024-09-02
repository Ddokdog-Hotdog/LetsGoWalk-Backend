package com.ddokdoghotdog.gowalk.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Product;
import com.ddokdoghotdog.gowalk.entity.ProductMemberLike;
import com.ddokdoghotdog.gowalk.entity.ProductMemberLike.ProductMemberLikeId;
import com.ddokdoghotdog.gowalk.global.config.s3.S3Service;
import com.ddokdoghotdog.gowalk.product.dto.ShopItemDeleteDTO;
import com.ddokdoghotdog.gowalk.product.dto.ShopItemRegisterRequestDTO;
import com.ddokdoghotdog.gowalk.product.dto.ShopItemUpdateRequestDTO;
import com.ddokdoghotdog.gowalk.product.repository.ProductMemberLikeRepository;
import com.ddokdoghotdog.gowalk.product.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

	@Autowired
	private S3Service s3Service;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductMemberLikeRepository productMemberLikeRepository;
	
	@Autowired
	private MemberRepository memberRepository;

	// 상품 등록
	public void productSave(ShopItemRegisterRequestDTO shopItemRegisterRequestDTO, MultipartFile thumbnail,
			MultipartFile detail) {
		log.info("상품 등록 정보 : {}", shopItemRegisterRequestDTO.toString());

		String thumbnailImageURL = s3Service.uploadFile(thumbnail, "products");
		String detailImageURL = s3Service.uploadFile(detail, "products");

		productRepository.save(Product.builder().categoryId(shopItemRegisterRequestDTO.getCategory()) // 카테고리 ID
				.name(shopItemRegisterRequestDTO.getName()) // 상품명
				.price(shopItemRegisterRequestDTO.getPrice()) // 가격
				.detailImage(detailImageURL) // 상품 설명 이미지 URL
				.stockQuantity(shopItemRegisterRequestDTO.getStockQuantity()) // 재고 수량
				.thumbnailImage(thumbnailImageURL) // 썸네일 이미지 URL
				.vendorId(shopItemRegisterRequestDTO.getVendor()) // 판매업체 ID
				.visible(true) // 상품 노출 여부
				.build());
	}

	// 상품 수정
	public void productUpdate(ShopItemUpdateRequestDTO shopItemUpdateRequestDTO, MultipartFile thumbnail,
			MultipartFile detail) {
		log.info("상품 수정 정보 : {}", shopItemUpdateRequestDTO.toString());
		Long id = shopItemUpdateRequestDTO.getProductId();
		Product product = productRepository.findById(id).get();
		if (shopItemUpdateRequestDTO.getIsStockUpdate() == 1) {
			log.info("재고만 수정");
			// 재고만 업데이트 하는 경우
			log.info("원래 product의 정보 : {}", product.toString());
			productRepository
					.save(product.toBuilder().stockQuantity(shopItemUpdateRequestDTO.getStockQuantity()).build());
		} else {
			// 상품 정보를 수정하는 경우
			s3Service.deleteFile(product.getThumbnailImage(), "products");
			s3Service.deleteFile(product.getDetailImage(), "products");

			String thumbnailImageURL = s3Service.uploadFile(thumbnail, "products");
			String detailImageURL = s3Service.uploadFile(detail, "products");

			productRepository.save(Product.builder().categoryId(shopItemUpdateRequestDTO.getCategory()) // 카테고리 ID
					.name(shopItemUpdateRequestDTO.getName()) // 상품명
					.price(shopItemUpdateRequestDTO.getPrice()) // 가격
					.detailImage(detailImageURL) // 상품 설명 이미지 URL
					.stockQuantity(shopItemUpdateRequestDTO.getStockQuantity()) // 재고 수량
					.thumbnailImage(thumbnailImageURL) // 썸네일 이미지 URL
					.vendorId(shopItemUpdateRequestDTO.getVendor()) // 판매업체 ID
					.visible(true) // 상품 노출 여부
					.build());
		}

	}

	// 상품 삭제
	public void productDelete(ShopItemDeleteDTO ShopItemDeleteDTO) {
		Long id = ShopItemDeleteDTO.getProductId();
		Product product = productRepository.findById(id).get();
		s3Service.deleteFile(product.getThumbnailImage(), "products");
		s3Service.deleteFile(product.getDetailImage(), "products");
		productRepository.delete(product);

	}

	// 쇼핑몰 메인 화면
	public Map<String, Object> getProductList(Integer page, Long memberId){
		List<Map<String, Object>> itemList = productRepository.getItemList(page, memberId);
		Map<String, Object> map = new HashMap<>();
		map.put("itemList", itemList);
		
		// best상품 넣어야함
		
		return map;
	}
	
	// 찜 목록 화면
	public Map<String, Object> getProductLikeList(Integer page, Long memberId){
		List<Map<String, Object>> itemList = productRepository.getLikeItemList(page, memberId);
		Map<String, Object> map = new HashMap<>();
		map.put("itemLikeList", itemList);
		return map;
	}

	// 상품 상세 페이지
	public Map<String, Object> getProductDetail(Long productId, Long memberId){
		Map<String, Object> itemDetail = productRepository.getItemDetail(productId, memberId);
		Map<String, Object> map = new HashMap<>();
		map.put("itemDetail", itemDetail);
		return map;
	}
	
	// 특정 상품에 좋아요 누르기
	@Transactional
	public void insertProductLike(Long productId, Long memberId) {
		Product product = productRepository.findById(productId).get();
		Member member = memberRepository.findById(memberId).get();
		productMemberLikeRepository.insertLike(product.getId(), member.getId());
	}
	
	// 특정 상품에 "찜" 해제
	@Transactional
	public void deleteProductLike(Long productId, Long memberId) {
		Product product = productRepository.findById(productId).get();
		Member member = memberRepository.findById(memberId).get();
		productMemberLikeRepository.deleteLike(product.getId(), member.getId());
	}

}
