package com.ddokdoghotdog.gowalk.cart.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.cart.dto.ShopCartsRequestDTO;
import com.ddokdoghotdog.gowalk.cart.dto.ShopCartsUpdateRequestDTO;
import com.ddokdoghotdog.gowalk.cart.repository.CartRepository;
import com.ddokdoghotdog.gowalk.entity.CartItem;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Product;
import com.ddokdoghotdog.gowalk.product.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	// 장바구니에 담기 - 이미 장바구니에 등록돼있는 상품이라면 수량 update만 해주는 방향으로
	public void insertCart(ShopCartsRequestDTO shopCartsRequestDTO, Long memberId) {
		Product products = productRepository.findById(shopCartsRequestDTO.getProductId()).get();
		Optional<CartItem> cartItem = cartRepository.findByMemberIdAndProductId(products, memberId);
		
		if(cartItem.isEmpty()) {
			// 장바구니에 없는 경우
			Product product = productRepository.findById(shopCartsRequestDTO.getProductId()).get();
			cartRepository.save(CartItem.builder()
					.quantity(shopCartsRequestDTO.getQuantity())
					.createdAt(new Timestamp(System.currentTimeMillis()))
					.product(product)
					.memberid(memberId)
					.build());
			
		}else {
			// 이미 장바구니에 있는 경우 수량만 업데이트 한다.
			CartItem cart = cartItem.get();
			cartRepository.save(cart.toBuilder()
					.quantity(cart.getQuantity() + shopCartsRequestDTO.getQuantity())
					.build());
		}
	}
	
	// 장바구니 메인 화면
	public Map<String, Object> getCartItemList(Long memberId){
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> cartItemList = cartRepository.getCartItemList(memberId);
		map.put("cartProducts", cartItemList);
		
		// 최근 배송 주소 불러와야함
		
		// 현재 보유 포인트 불러와야함
		
		return map;
		
	}
	
	// 장바구니 수량 변경
	public void updateCart(ShopCartsUpdateRequestDTO shopCartsUpdateRequestDTO, Long memberId) {
		CartItem cartItem = cartRepository.findById(shopCartsUpdateRequestDTO.getCartItemId()).get();
		cartRepository.save(cartItem.toBuilder()
									.quantity(shopCartsUpdateRequestDTO.getQuantity())
									.build());
	}
	
	
	// 장바구니에서 제거
	public void deleteCart(ShopCartsUpdateRequestDTO shopCartsUpdateRequestDTO, Long memberId) {
		CartItem cartItem = cartRepository.findById(shopCartsUpdateRequestDTO.getCartItemId()).get();
		cartRepository.delete(cartItem);
	}
	
	
}
