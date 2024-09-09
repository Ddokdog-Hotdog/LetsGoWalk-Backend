package com.ddokdoghotdog.gowalk.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.entity.Category;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Product;
import com.ddokdoghotdog.gowalk.entity.ProductMemberLike;
import com.ddokdoghotdog.gowalk.entity.Vendor;
import com.ddokdoghotdog.gowalk.global.config.s3.S3Service;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderItemDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderRequestDTO;
import com.ddokdoghotdog.gowalk.product.dto.ShopItemDeleteDTO;
import com.ddokdoghotdog.gowalk.product.dto.ShopItemRegisterRequestDTO;
import com.ddokdoghotdog.gowalk.product.dto.ShopItemUpdateRequestDTO;
import com.ddokdoghotdog.gowalk.product.repository.CategoryRepository;
import com.ddokdoghotdog.gowalk.product.repository.ProductMemberLikeRepository;
import com.ddokdoghotdog.gowalk.product.repository.ProductRepository;
import com.ddokdoghotdog.gowalk.product.repository.VendorRepository;

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
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private VendorRepository vendorRepository;

	// 상품 등록
	@Transactional
	public void productSave(ShopItemRegisterRequestDTO shopItemRegisterRequestDTO, MultipartFile thumbnail,
			MultipartFile detail) {
		log.info("상품 등록 정보 : {}", shopItemRegisterRequestDTO.toString());
		
		// 카테고리 있는지 여부 확인
		Long categoryId = shopItemRegisterRequestDTO.getCategory();
		categoryRepository.findById(categoryId)
			.orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
		
		// 판매업체 실제 있는지 확인
		Long vendorId = shopItemRegisterRequestDTO.getVendor();
		vendorRepository.findById(vendorId)
		.orElseThrow(() -> new BusinessException(ErrorCode.VENDOR_NOT_FOUND));
		

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
	@Transactional
	public void productUpdate(ShopItemUpdateRequestDTO shopItemUpdateRequestDTO, MultipartFile thumbnail,
			MultipartFile detail) {
		log.info("상품 수정 정보 : {}", shopItemUpdateRequestDTO.toString());
		Long id = shopItemUpdateRequestDTO.getProductId();
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
		
		if (shopItemUpdateRequestDTO.getIsStockUpdate() == 1) {
			log.info("재고만 수정");
			// 재고만 업데이트 하는 경우
			log.info("원래 product의 정보 : {}", product.toString());
			productRepository
					.save(product.toBuilder().stockQuantity(shopItemUpdateRequestDTO.getStockQuantity()).build());
		} else {
			// 카테고리 있는지 여부 확인
			Long categoryId = shopItemUpdateRequestDTO.getCategory();
			categoryRepository.findById(categoryId)
				.orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
			
			// 판매업체 실제 있는지 확인
			Long vendorId = shopItemUpdateRequestDTO.getVendor();
			vendorRepository.findById(vendorId)
			.orElseThrow(() -> new BusinessException(ErrorCode.VENDOR_NOT_FOUND));
			
			
			// 상품 정보를 수정하는 경우
			s3Service.deleteFile(product.getThumbnailImage(), "products");
			s3Service.deleteFile(product.getDetailImage(), "products");

			String thumbnailImageURL = s3Service.uploadFile(thumbnail, "products");
			String detailImageURL = s3Service.uploadFile(detail, "products");

			productRepository.save(product.toBuilder().categoryId(categoryId) // 카테고리 ID
					.name(shopItemUpdateRequestDTO.getName()) // 상품명
					.price(shopItemUpdateRequestDTO.getPrice()) // 가격
					.detailImage(detailImageURL) // 상품 설명 이미지 URL
					.stockQuantity(shopItemUpdateRequestDTO.getStockQuantity()) // 재고 수량
					.thumbnailImage(thumbnailImageURL) // 썸네일 이미지 URL
					.vendorId(vendorId) // 판매업체 ID
					.visible(true) // 상품 노출 여부
					.build());
		}

	}

	// 상품 삭제
	@Transactional
	public void productDelete(ShopItemDeleteDTO ShopItemDeleteDTO) {
		Long id = ShopItemDeleteDTO.getProductId();
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
		
//		s3Service.deleteFile(product.getThumbnailImage(), "products");
//		s3Service.deleteFile(product.getDetailImage(), "products");
		productRepository.updateVisible(0, id);
//		productRepository.delete(product);

	}

	// 쇼핑몰 메인 화면
	public Map<String, Object> getProductList(Integer page, Long memberId){
		List<Map<String, Object>> itemList = productRepository.getItemList(page, memberId);
		Map<String, Object> map = new HashMap<>();
		map.put("itemList", itemList);	
		return map;
	}
	
	// 쇼핑몰 인기 상품 조회
	public Map<String, Object> getBestProductList(Long memberId){
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> bestItemList = productRepository.findBestItemList(memberId);
		map.put("bestItemList", bestItemList);
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
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
		
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
		
		// 이미 좋아요를 눌렀는지 확인
		Optional<ProductMemberLike> pml = productMemberLikeRepository.findByProductAndMember(product, member);
		if(pml.isPresent()) {
			// 이미 좋아요를 눌렀다는 의미
			throw new BusinessException(ErrorCode.ALREADY_LIKE_REQUEST);
		}
		
		productMemberLikeRepository.insertLike(product.getId(), member.getId());
	}
	
	// 특정 상품에 "찜" 해제
	@Transactional
	public void deleteProductLike(Long productId, Long memberId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
		
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
		
		// 이미 좋아요를 해제 했는지 확인 => 예외가 발생하면 이미 없다는 의미
		productMemberLikeRepository.findByProductAndMember(product, member)
				.orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_LIKE_NOT_FOUND));
		
		productMemberLikeRepository.deleteLike(product.getId(), member.getId());
	}
	
	
	// 판매업체 등록
	public void insertVendor(String vendorName) {
		Optional<Vendor> vendor = vendorRepository.findByName(vendorName);
		if(vendor.isPresent()) {
			// 이미 동일한 이름으로 판매업체를 등록한 경우
			throw new BusinessException(ErrorCode.ALREADY_VENDOR_REQUEST);
		}
		
		vendorRepository.save(Vendor.builder()
					.name(vendorName)
					.build());
	}
	
	// 판매업체명 변경
	public void updateVendor(String updateVendorName, Long vendorId) {
		Vendor vendor = vendorRepository.findById(vendorId)
				.orElseThrow(() -> new BusinessException(ErrorCode.VENDOR_NOT_FOUND));
		vendorRepository.save(vendor.toBuilder()
					.name(updateVendorName)
					.build());
	}
	
	// 판매업체 목록 가져오기
	public List<Vendor> findVendorAll(){
		return vendorRepository.findAll();
	}
	
	// 판매업체 삭제
	public void deleteVendor(Long vendorId) {
		Vendor vendor = vendorRepository.findById(vendorId)
				.orElseThrow(() -> new BusinessException(ErrorCode.VENDOR_NOT_FOUND));
		try {
			vendorRepository.delete(vendor);			
		} catch (Exception e) {
			// 이미 해당 판매업체로 등록된 상품이 있다는 의미
			throw new BusinessException(ErrorCode.VENDOR_DELETE_ERROR);
		}
	}
	
	// 카테고리 등록
	public void insertCategory(String categoryName) {
		Optional<Category> category = categoryRepository.findByName(categoryName);
		if(category.isPresent()) {
			// 이미 동일한 이름으로 카테고리를 등록한 경우
			throw new BusinessException(ErrorCode.ALREADY_CATEGORY_REQUEST);
		}
		
		categoryRepository.save(Category.builder()
									.name(categoryName)
									.build());
	}
	
	// 카테고리명 변경
	public void updateCategory(String updateCategoryName, Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
		categoryRepository.save(category.toBuilder()
									.name(updateCategoryName)
									.build());
	}
	
	// 카테고리 목록 가져오기
	public List<Category> findCategoryAll() {
		return categoryRepository.findAll();
	}
	
	// 카테고리 삭제
	public void deleteCategory(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND));
		try {
			categoryRepository.delete(category);
		}catch (Exception e) {
			// 이미 해당 카테고리로 등록된 상품이 있다는 의미
			throw new BusinessException(ErrorCode.CATEGORY_DELETE_ERROR);
		}
	}
	
	// 결제 요청이 온 건에 대한 가격 검증
	@Transactional
	public void inspectShopOrderRequestDTO(ShopOrderRequestDTO shopOrderRequestDTO) {
		Long totalPrice = 0L;
		List<ShopOrderItemDTO> orderItemList = shopOrderRequestDTO.getOrderItems();
		for(ShopOrderItemDTO orderItem : orderItemList) {
			// 주문 요청이 온 상픔이 진짜 있는 지 확인
			Long productId = orderItem.getProductId();
			Product product = productRepository.findById(productId)
					.orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
			totalPrice += product.getPrice() * orderItem.getQuantity();
		}
		
		// 주문 요청이 들어온 총 가격과 DB상에 저장돼있는 총 가격이 맞는지 확인
		if(Integer.parseInt(String.valueOf(totalPrice)) != shopOrderRequestDTO.getTotalAmount()) {
			// 틀리다면 가격에 장난질을 한 것임
			log.info("가격에 장난질하지 마십쇼!!");
			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
		}
		
	}
	
}
