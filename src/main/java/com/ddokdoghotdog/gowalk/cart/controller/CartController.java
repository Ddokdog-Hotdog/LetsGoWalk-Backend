package com.ddokdoghotdog.gowalk.cart.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.cart.dto.ShopCartsRequestDTO;
import com.ddokdoghotdog.gowalk.cart.dto.ShopCartsUpdateRequestDTO;
import com.ddokdoghotdog.gowalk.cart.service.CartService;
import com.ddokdoghotdog.gowalk.global.annotation.RequiredMemberId;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/shop/carts")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@RequiredMemberId
	@PostMapping("register")
	public ResponseEntity<?> insertCart(@RequestBody ShopCartsRequestDTO shopCartsRequestDTO, Long memberId){
		cartService.insertCart(shopCartsRequestDTO, memberId);
		return ResponseEntity.status(HttpStatus.OK)
						.body("장바구니 등록 성공");
	}
	
	@RequiredMemberId
	@GetMapping("")
	public ResponseEntity<?> getCartItemList(Long memberId){
		Map<String,Object> cartItemList = cartService.getCartItemList(memberId);
		return ResponseEntity.status(HttpStatus.OK)
						.body(cartItemList);
	}
	
	@RequiredMemberId
	@PutMapping("update")
	public ResponseEntity<?> updateCartItem(@RequestBody ShopCartsUpdateRequestDTO shopCartsUpdateRequestDTO, Long memberId){
		cartService.updateCart(shopCartsUpdateRequestDTO, memberId);
		return ResponseEntity.status(HttpStatus.OK)
						.body("장바구니 수량 변경 완료");
	}
	
	@RequiredMemberId
	@DeleteMapping("delete")
	public ResponseEntity<?> deleteCartItem(@RequestBody ShopCartsUpdateRequestDTO shopCartsUpdateRequestDTO, Long memberId){
		cartService.deleteCart(shopCartsUpdateRequestDTO, memberId);
		return ResponseEntity.status(HttpStatus.OK)
						.body("장바구니에서 제거 완료");
	}
	
	
}
