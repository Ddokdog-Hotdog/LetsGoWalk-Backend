package com.ddokdoghotdog.gowalk.entity.initializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Category;
import com.ddokdoghotdog.gowalk.entity.Product;
import com.ddokdoghotdog.gowalk.entity.Vendor;
import com.ddokdoghotdog.gowalk.product.repository.CategoryRepository;
import com.ddokdoghotdog.gowalk.product.repository.ProductRepository;
import com.ddokdoghotdog.gowalk.product.repository.VendorRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Profile("dev")
public class ProductInitalizer {

	private final VendorRepository vendorRepository;
	private final CategoryRepository categoryRepository;
	private final ProductRepository productRepository;
	
	
	@Bean
	@Transactional
	public CommandLineRunner initalizeProducts() {
		return arge -> {
			if(vendorRepository.findByName("DDogHotdog").isEmpty()) {
				vendorRepository.save(Vendor.builder()
						.name("DDogHotdog")
						.build());
			}
			
			if(categoryRepository.findByName("walking").isEmpty()) {
				categoryRepository.save(Category.builder()
						.name("walking")
						.build());
			}
			
			products.forEach(product -> {
				if(productRepository.findByName((String) product.get("상품명")).isEmpty()) {
					productRepository.save(Product.builder()
							.name((String) product.get("상품명"))
							.thumbnailImage((String) product.get("썸네일"))
							.detailImage((String) product.get("상세정보"))
							.stockQuantity((Long) product.get("재고"))
							.price((Long) product.get("가격"))
							.vendorId(1L)
							.categoryId(1L)
							.visible(true)
							.build());
				}
			});
		};
	}
	
	private final static List<Map<String, Object>> products = new ArrayList<>();
	
	static {
		Map<String, Object> product1 = new HashMap<>();
        product1.put("상품명", "[Y-하네스] 폼 하네스");
        product1.put("썸네일", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/%5BY-%ED%95%98%EB%84%A4%EC%8A%A4%5D+%ED%8F%BC+%ED%95%98%EB%84%A4%EC%8A%A4+(5%EC%BB%AC%EB%9F%AC)_%EC%8D%B8%EB%84%A4%EC%9D%BC.png");
        product1.put("상세정보", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/products/%5BY-%ED%95%98%EB%84%A4%EC%8A%A4%5D+%ED%8F%BC+%ED%95%98%EB%84%A4%EC%8A%A4+(5%EC%BB%AC%EB%9F%AC)_%EC%83%81%ED%92%88%EC%A0%95%EB%B3%B4.png");
        product1.put("재고", 10000L);
        product1.put("가격", 50000L);
        products.add(product1);

        Map<String, Object> product2 = new HashMap<>();
        product2.put("상품명", "[반초크 기능] 스타 목줄");
        product2.put("썸네일", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/%5B%EB%B0%98%EC%B4%88%ED%81%AC+%EA%B8%B0%EB%8A%A5%5D+%EC%8A%A4%ED%83%80+%EB%AA%A9%EC%A4%84+(4%EC%BB%AC%EB%9F%AC)_%EC%8D%B8%EB%84%A4%EC%9D%BC.png");
        product2.put("상세정보", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/products/%5B%EB%B0%98%EC%B4%88%ED%81%AC+%EA%B8%B0%EB%8A%A5%5D+%EC%8A%A4%ED%83%80+%EB%AA%A9%EC%A4%84+(4%EC%BB%AC%EB%9F%AC)_%EC%83%81%ED%92%88%EC%A0%95%EB%B3%B4.png");
        product2.put("재고", 10000L);
        product2.put("가격", 43000L);
        products.add(product2);

        Map<String, Object> product3 = new HashMap<>();
        product3.put("상품명", "UV 자외선 차단 셔츠");
        product3.put("썸네일", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/UV+%EC%9E%90%EC%99%B8%EC%84%A0+%EC%B0%A8%EB%8B%A8+%EC%85%94%EC%B8%A0_%EC%8D%B8%EB%84%A4%EC%9D%BC.png");
        product3.put("상세정보", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/products/UV+%EC%9E%90%EC%99%B8%EC%84%A0+%EC%B0%A8%EB%8B%A8+%EC%85%94%EC%B8%A0_%EC%83%81%ED%92%88%EC%A0%95%EB%B3%B4.png");
        product3.put("재고", 10000L);
        product3.put("가격", 65000L);
        products.add(product3);

        Map<String, Object> product4 = new HashMap<>();
        product4.put("상품명", "구명조끼");
        product4.put("썸네일", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/%EA%B5%AC%EB%AA%85%EC%A1%B0%EB%81%BC_%EC%8D%B8%EB%84%A4%EC%9D%BC.png");
        product4.put("상세정보", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/products/%EA%B5%AC%EB%AA%85%EC%A1%B0%EB%81%BC_%EC%83%81%ED%92%88%EC%A0%95%EB%B3%B4.png");
        product4.put("재고", 10000L);
        product4.put("가격", 84000L);
        products.add(product4);

        Map<String, Object> product5 = new HashMap<>();
        product5.put("상품명", "마이크로 배스로브");
        product5.put("썸네일", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C+%EB%B0%B0%EC%8A%A4%EB%A1%9C%EB%B8%8C_%EC%8D%B8%EB%84%A4%EC%9D%BC.png");
        product5.put("상세정보", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/products/%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C+%EB%B0%B0%EC%8A%A4%EB%A1%9C%EB%B8%8C_%EC%83%81%ED%92%88%EC%A0%95%EB%B3%B4.png");
        product5.put("재고", 10000L);
        product5.put("가격", 92000L);
        products.add(product5);

        Map<String, Object> product6 = new HashMap<>();
        product6.put("상품명", "미니 컴포트 에어 하네스");
        product6.put("썸네일", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/%EB%AF%B8%EB%8B%88+%EC%BB%B4%ED%8F%AC%ED%8A%B8+%EC%97%90%EC%96%B4+%ED%95%98%EB%84%A4%EC%8A%A4_%EC%8D%B8%EB%84%A4%EC%9D%BC.png");
        product6.put("상세정보", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/products/%EB%AF%B8%EB%8B%88+%EC%BB%B4%ED%8F%AC%ED%8A%B8+%EC%97%90%EC%96%B4+%ED%95%98%EB%84%A4%EC%8A%A4_%EC%83%81%ED%92%88%EC%A0%95%EB%B3%B4.png");
        product6.put("재고", 10000L);
        product6.put("가격", 102000L);
        products.add(product6);

        Map<String, Object> product7 = new HashMap<>();
        product7.put("상품명", "세이프티 부츠");
        product7.put("썸네일", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/%EC%84%B8%EC%9D%B4%ED%94%84%ED%8B%B0+%EB%B6%80%EC%B8%A0_%EC%8D%B8%EB%84%A4%EC%9D%BC.png");
        product7.put("상세정보", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/products/%EC%84%B8%EC%9D%B4%ED%94%84%ED%8B%B0+%EB%B6%80%EC%B8%A0_%EC%83%81%ED%92%88%EC%A0%95%EB%B3%B4.png");
        product7.put("재고", 10000L);
        product7.put("가격", 98000L);
        products.add(product7);

        Map<String, Object> product8 = new HashMap<>();
        product8.put("상품명", "스타 리쉬");
        product8.put("썸네일", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/%EC%8A%A4%ED%83%80+%EB%A6%AC%EC%89%AC+(4%EC%BB%AC%EB%9F%AC)_%EC%8D%B8%EB%84%A4%EC%9D%BC.png");
        product8.put("상세정보", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/products/%EC%8A%A4%ED%83%80+%EB%A6%AC%EC%89%AC+(4%EC%BB%AC%EB%9F%AC)_%EC%83%81%ED%92%88%EC%A0%95%EB%B3%B4.png");
        product8.put("재고", 10000L);
        product8.put("가격", 78000L);
        products.add(product8);

        Map<String, Object> product9 = new HashMap<>();
        product9.put("상품명", "스트림레인자켓 옐로우");
        product9.put("썸네일", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/%EC%8A%A4%ED%8A%B8%EB%A6%BC%EB%A0%88%EC%9D%B8%EC%9E%90%EC%BC%93+%EC%98%90%EB%A1%9C%EC%9A%B0_%EC%8D%B8%EB%84%A4%EC%9D%BC.png");
        product9.put("상세정보", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/products/%EC%8A%A4%ED%8A%B8%EB%A6%BC%EB%A0%88%EC%9D%B8%EC%9E%90%EC%BC%93+%EC%98%90%EB%A1%9C%EC%9A%B0_%EC%83%81%ED%92%88%EC%A0%95%EB%B3%B4.png");
        product9.put("재고", 10000L);
        product9.put("가격", 46000L);
        products.add(product9);

        Map<String, Object> product10 = new HashMap<>();
        product10.put("상품명", "지브라 오버올");
        product10.put("썸네일", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail/%EC%A7%80%EB%B8%8C%EB%9D%BC+%EC%98%A4%EB%B2%84%EC%98%AC_%EC%8D%B8%EB%84%A4%EC%9D%BC.png");
        product10.put("상세정보", "https://todotodo-bucket.s3.ap-northeast-2.amazonaws.com/products/%EC%A7%80%EB%B8%8C%EB%9D%BC+%EC%98%A4%EB%B2%84%EC%98%AC_%EC%83%81%ED%92%88%EC%A0%95%EB%B3%B4.png");
        product10.put("재고", 10000L);
        product10.put("가격", 98000L);
        products.add(product10);
	}
	
}
