package com.ddokdoghotdog.gowalk.payment.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.cart.repository.CartRepository;
import com.ddokdoghotdog.gowalk.entity.CartItem;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Order;
import com.ddokdoghotdog.gowalk.entity.OrderItem;
import com.ddokdoghotdog.gowalk.entity.Payment;
import com.ddokdoghotdog.gowalk.entity.Product;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderItemDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderRequestDTO;
import com.ddokdoghotdog.gowalk.payment.repository.OrderItemsRepository;
import com.ddokdoghotdog.gowalk.payment.repository.OrdersRepository;
import com.ddokdoghotdog.gowalk.payment.repository.PaymentRepository;
import com.ddokdoghotdog.gowalk.product.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentService {

	@Autowired
	private OrdersRepository ordersRepository;
	
	@Autowired
	private OrderItemsRepository orderItemsRepository;
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Transactional
	public void updateStockAndInsertOrderItem(ShopOrderItemDTO orderItem, String orderId) {
		// 재고 확인 및 재고 업데이트
		
		
		long productid = orderItem.getProductId();
//		Product product = productRepository.findById(productid).get();
//		
//		if(product.getStockQuantity() - orderItem.getQuantity() < 0) {
//			throw new RuntimeException("재고 부족");
//		}
//		
//		product.toBuilder()
//				.stockQuantity(product.getStockQuantity() - orderItem.getQuantity())
//				.build();
//		productRepository.save(product);
		
		Product product = productRepository.findByIdForUpdate(productid)
				.orElseThrow(() -> new EntityNotFoundException("찾을 수 없습니다."));
		
		if(product.getStockQuantity() <= 0 || product.getStockQuantity() - orderItem.getQuantity() < 0) {
			throw new RuntimeException("재고가 부족하여 결제를 할 수 없습니다.");
		}
		
		product.toBuilder()
				.stockQuantity(product.getStockQuantity() - orderItem.getQuantity())
				.build();
		productRepository.save(product);
		log.info("재고 업데이트 완료");
		
		
		Order order = ordersRepository.findByKakaoOrderId(orderId)
				.orElseThrow(() -> new EntityNotFoundException("찾을 수 없습니다."));
		
		// 주문상품에 insert
		OrderItem item = OrderItem.builder()
								.quantity(orderItem.getQuantity())
								.createdAt(new Timestamp(System.currentTimeMillis()))
								.product(product)
								.order(order)
								.build();
		orderItemsRepository.save(item);
		log.info("주문 상품 테이블 insert 완료");
	}
	
	@Transactional
	public void insertOrderAndInsertPayment(ShopOrderRequestDTO shopOrderRequestDTO, Long memberId, 
			String orderId, String tid) {
		
		Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new EntityNotFoundException("찾을 수 없습니다."));
		
		// 주문테이블에 insert
		Order order = Order.builder()
						.address(shopOrderRequestDTO.getAddress())
						.orderDat(new Timestamp(System.currentTimeMillis()))
						.member(member)
						.kakaoOrderId(orderId)
						.build();
		
		ordersRepository.save(order);
		log.info("주문 테이블 insert 완료");
		
		// 결제테이블에 insert
		Payment payment = Payment.builder()
							.payAmount(Long.parseLong(String.valueOf(shopOrderRequestDTO.getTotalAmount())))
							.paidAt(new Timestamp(System.currentTimeMillis()))
							.order(order)
							.pointAmount(shopOrderRequestDTO.getPoint())
							.kakaoOrderId(tid)
							.build();
		
		paymentRepository.save(payment);
		log.info("결제 테이블 insert 완료");
		
		if(member.getPoint() <= 0 && shopOrderRequestDTO.getPoint() > 0) {
			throw new RuntimeException("포인트 부족");
			
		}else if(shopOrderRequestDTO.getPoint() > 0) {
			member.toBuilder()
			.point(member.getPoint() - shopOrderRequestDTO.getPoint())
			.build();
		}
		
		// 멤버테이블에 포인트 갱신
		String numString = String.valueOf(shopOrderRequestDTO.getTotalAmount() * 0.1);
		// 소수점 이하 제거
        String processedStr = numString.split("\\.")[0];
        // Long으로 변환
        //Long number = Long.parseLong(processedStr);
        
		Long addPoint = Long.parseLong(processedStr);
		log.info("적립되는 포인트 : {}", addPoint.toString());
		member.toBuilder()
			.point(member.getPoint() + addPoint)
			.build();
		memberRepository.save(member);
		log.info("회원 테이블에서 포인트 갱신 완료");
		
		// 장바구니 제거
		List<ShopOrderItemDTO> orderItems = shopOrderRequestDTO.getOrderItems();
		for(ShopOrderItemDTO cartItem : orderItems) {
			CartItem cart = cartRepository.findById(cartItem.getCartItemId())
				.orElseThrow(() -> new EntityNotFoundException("장바구니에서 찾을 수 없습니다."));
			cartRepository.delete(cart);
		}
		log.info("장바구니에서 결제 목록들 제거 완료");
	}
	
}
