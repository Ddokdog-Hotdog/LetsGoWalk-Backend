package com.ddokdoghotdog.gowalk.payment.service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderItemDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderRequestDTO;

@Service
public class PaymentSychService {

	@Autowired
	private PaymentService paymentService;
	
	private static final ConcurrentHashMap<Long, Integer> productLockMap = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<Long, Object> lockObjects = new ConcurrentHashMap<>();
	
	/*
	 * memberId => 유저id
	 * orderId => 주문테이블의 kakaoid컬럼에 들어갈 값
	 * tid => 결제테이블의 kakaoid컬럼에 들어갈 값
	 * ShopOrderRequestDTO => 주문요청에 대한 정보
	 * 
	 */
	@Transactional
	public void processProduct(ShopOrderRequestDTO shopOrderRequestDTO, Long memberId, 
			String orderId, String tid) {
        
		// 주문 테이블 및 결제 테이블에 insert
		paymentService.insertOrderAndInsertPayment(shopOrderRequestDTO, memberId, orderId, tid);
		
		// productId를 기준으로 오름차순 정렬
		List<ShopOrderItemDTO> orderItemList = shopOrderRequestDTO.getOrderItems();
		orderItemList.sort(Comparator.comparing(ShopOrderItemDTO::getProductId));
		
		// 요청이 들어온 상품인지 확인
		for(ShopOrderItemDTO orderItem : orderItemList) {
	        Integer isProcessing = productLockMap.putIfAbsent(orderItem.getProductId(), 1);
	        
	        // 이미 다른 스레드에서 처리 중인 경우
	        if (isProcessing != null && isProcessing == 1) {
	            synchronized (getLockObject(orderItem.getProductId())) {
	                // 상품에 대한 처리 로직 (이미 요청이 들어온 경우)
	                // 여기서는 synchronized 블록 안에서 처리
	                System.out.println("상품 " + orderItem.getProductId() + " 처리 중...");
	                
	                // 재고 업데이트, 주문 상품 테이블에 insert
	                paymentService.updateStockAndInsertOrderItem(orderItem, orderId);
	                
	                // 처리 완료 후 상태를 초기화
	                productLockMap.put(orderItem.getProductId(), 0);
	            }
	        } else {
	            // 상품에 대한 처리 로직 (요청이 들어오지 않은 경우)
	            // 여기서는 synchronized 블록 밖에서 처리 가능
	            System.out.println("상품 " + orderItem.getProductId() + "을(를) 처리 시작...");
	            
	            // 새 요청에 대해 상태를 업데이트
	            productLockMap.put(orderItem.getProductId(), 1);
	            
	            synchronized (getLockObject(orderItem.getProductId())) {
	            	
	            	// 재고 업데이트
	            	paymentService.updateStockAndInsertOrderItem(orderItem, orderId);
	            	
		    		productLockMap.put(orderItem.getProductId(), 0);
	            }
	            
	        }
		}
		
		
		
    }

    private Object getLockObject(Long productId) {
        // 상품별로 동기화를 위해 고유한 락 객체를 반환합니다.
        return lockObjects.computeIfAbsent(productId, key -> new Object());
    }
    
    
    public void processProduct(ShopOrderItemDTO orderItem, Long memberId, 
			String orderId, String tid) {
    	Integer isProcessing = productLockMap.putIfAbsent(orderItem.getProductId(), 1);
        
        // 이미 다른 스레드에서 처리 중인 경우
        if (isProcessing != null && isProcessing == 1) {
            synchronized (getLockObject(orderItem.getProductId())) {
                // 상품에 대한 처리 로직 (이미 요청이 들어온 경우)
                // 여기서는 synchronized 블록 안에서 처리
                System.out.println("상품 " + orderItem.getProductId() + " 처리 중...");
                
                // 재고 업데이트, 주문 상품 테이블에 insert
                paymentService.updateStockAndInsertOrderItem(orderItem, orderId);
                
                // 처리 완료 후 상태를 초기화
                productLockMap.put(orderItem.getProductId(), 0);
            }
        } else {
            // 상품에 대한 처리 로직 (요청이 들어오지 않은 경우)
            // 여기서는 synchronized 블록 밖에서 처리 가능
            System.out.println("상품 " + orderItem.getProductId() + "을(를) 처리 시작...");
            
            // 새 요청에 대해 상태를 업데이트
            productLockMap.put(orderItem.getProductId(), 1);
            
            synchronized (getLockObject(orderItem.getProductId())) {
            	
            	// 재고 업데이트
            	paymentService.updateStockAndInsertOrderItem(orderItem, orderId);
            	
	    		productLockMap.put(orderItem.getProductId(), 0);
            }
            
        }
    }
}
