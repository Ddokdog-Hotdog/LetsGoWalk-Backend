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

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/shop/carts")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@PostMapping("register")
	public ResponseEntity<?> insertCart(@RequestBody ShopCartsRequestDTO shopCartsRequestDTO){
		// 멤버id 넣는 과정 필요
		long memberId = 2;
		cartService.insertCart(shopCartsRequestDTO, memberId);
		return ResponseEntity.status(HttpStatus.OK)
						.body("장바구니 등록 성공");
	}
	
	
	@GetMapping("")
	public ResponseEntity<?> getCartItemList(){
		// 멤버id 넣는 과정 필요
		long memberId = 2;
		Map<String,Object> cartItemList = cartService.getCartItemList(memberId);
		return ResponseEntity.status(HttpStatus.OK)
						.body(cartItemList);
	}
	
	@PutMapping("update")
	public ResponseEntity<?> updateCartItem(@RequestBody ShopCartsUpdateRequestDTO shopCartsUpdateRequestDTO){
		// 멤버id 넣는 과정 필요
		long memberId = 2;
		cartService.updateCart(shopCartsUpdateRequestDTO, memberId);
		return ResponseEntity.status(HttpStatus.OK)
						.body("장바구니 수량 변경 완료");
	}
	
	@DeleteMapping("delete")
	public ResponseEntity<?> deleteCartItem(@RequestBody ShopCartsUpdateRequestDTO shopCartsUpdateRequestDTO){
		// 멤버id 넣는 과정 필요
		long memberId = 2;
		cartService.deleteCart(shopCartsUpdateRequestDTO, memberId);
		return ResponseEntity.status(HttpStatus.OK)
						.body("장바구니에서 제거 완료");
	}
	
	
}
