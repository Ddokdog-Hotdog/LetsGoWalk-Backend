package com.ddokdoghotdog.gowalk.payment.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Order;
import com.ddokdoghotdog.gowalk.entity.OrderItem;
import com.ddokdoghotdog.gowalk.entity.Payment;
import com.ddokdoghotdog.gowalk.entity.Product;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.payment.dto.ShopCancelRequestDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderItemDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderRequestDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopRefundRequestDTO;
import com.ddokdoghotdog.gowalk.payment.repository.OrderItemsRepository;
import com.ddokdoghotdog.gowalk.payment.repository.PaymentRepository;
import com.ddokdoghotdog.gowalk.pet.repository.BreedRepository;
import com.ddokdoghotdog.gowalk.pet.repository.PetRepository;
import com.ddokdoghotdog.gowalk.product.repository.ProductRepository;
import com.ddokdoghotdog.gowalk.product.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentParentService {

    @Value("${cid}")
    private String cid;
    
	
	private final PaymentSychService paymentSychService;
	private final PaymentService paymentService;
	private final OrderItemService orderItemService;
	private final OrderService orderService;
	private final ProductService productService;
	private final ProductRepository productRepository;
	private final MemberRepository memberRepository;
	private final PaymentRepository paymentRepository;
	private final OrderItemsRepository orderItemsRepository;
	
	@Transactional
	public void processProduct(ShopOrderRequestDTO shopOrderRequestDTO, Long memberId, 
			String orderId, String tid) {
		
		// 주문 테이블 및 결제 테이블에 insert
		// 포인트 갱신 및 장바구니에서 주문 상품 제거
		paymentService.insertOrderAndInsertPayment(shopOrderRequestDTO, memberId, orderId, tid);
				
		// productId를 기준으로 오름차순 정렬
		List<ShopOrderItemDTO> orderItemList = shopOrderRequestDTO.getOrderItems();
		orderItemList.sort(Comparator.comparing(ShopOrderItemDTO::getProductId));
		
		// 주문 상품별로 재고update 및 주문테이블에 insert
		for(ShopOrderItemDTO orderItem : orderItemList) {
			paymentService.updateStockAndInsertOrderItem(orderItem, orderId);
			// paymentSychService.processProduct(orderItem, memberId, orderId, tid);
			
		}
		
	}
	
	@Transactional
	public ShopCancelRequestDTO refund(ShopRefundRequestDTO shopRefundRequestDTO, Long memberId) {
    	// 1. 넘어온 orderItemId가 정말 있는지 확인
    	OrderItem orderItem = orderItemService.findByOrderItemId(shopRefundRequestDTO.getOrderItemId());
    	
    	if(orderItem.getStatus() == false) {
    		// 이미 환불을 마친 주문 상품
    		throw new BusinessException(ErrorCode.ALREADY_REFUND_REQUEST);
    	}
    	
    	// 2. 넘어온 orderItem과 memberId가 맞는지
    	Order order = orderService.findById(orderItem.getOrder().getId());
    	Member member = order.getMember();
    	if(member.getId() != memberId) {
    		// 실제 요청자와 DB상에 있는 주문한 사람과 맞는지 확인
    		log.info("DB상에 있는 주문자 정보 : {}", member.getId());
    		log.info("요청자의 id : {}", memberId);
    		
    		throw new BusinessException(ErrorCode.INVALID_REFUND_REQUEST);
    	}
    	
    	// 3. orderItemId로 결제테이블의 tid가져오기, 포인트 사용했는지도 가져오기
    	Payment payment = paymentService.findByOrder(order);
    	log.info("tid 내용 : {}", payment.getKakaoOrderId());
    	log.info("포인트 사용 금액 : {} ", payment.getPointAmount());
    	
    	// 4. orderItem과 product를 통해 환불해야할 가격 가져오기
    	Product product = orderItem.getProduct();
    	Long refundPrice = product.getPrice() * orderItem.getQuantity();
    	log.info("환불 가격 : {}", refundPrice);
    	
    	// 5. orderItem의 status 0으로 바꾸기
    	orderItemsRepository.save(orderItem.toBuilder()
		    			.status(false)
		    			.build());
    	log.info("주문 상품 테이블의 상태 환불로 변경");
    	
    	// 5-1. 포인트를 사용했다면 멤버테이블의 포인트도 다시 수정
    	if(payment.getPointAmount() > 0) {
    		memberRepository.save(member.toBuilder()
		        			.point(member.getPoint() + payment.getPointAmount())
		        			.build());
    		refundPrice -= payment.getPointAmount();
    		log.info("멤버 테이블 포인트 업데이트 완료");
    	}
    	
    	// 6. 결제테이블의 결제가격도 바꾸기 또는 포인트 사용했다면 포인트도 바꾸기
    	paymentRepository.save(payment.toBuilder()
		    			.payAmount(payment.getPayAmount() - refundPrice)
		    			.pointAmount(0L)
		    			.build());
    	log.info("결제 테이블 수정 완료");
    	
    	// 7. 상품테이블의 재고 복구 -> 비관적락
		Product products = productRepository.findByIdForUpdate(product.getId())
				.orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
		productRepository.save(products.toBuilder()
						.stockQuantity(products.getStockQuantity() + orderItem.getQuantity())
						.build());
		log.info("재고 업데이트 완료");
		
		
		return ShopCancelRequestDTO.builder()
						.tid(payment.getKakaoOrderId())
						.cancelAmount(Integer.parseInt(String.valueOf(refundPrice)))
						.cancelTaxFreeAmount(0)
						.cid(cid)
						.build();
		
	}
}
