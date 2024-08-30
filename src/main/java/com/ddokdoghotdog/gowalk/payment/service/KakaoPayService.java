package com.ddokdoghotdog.gowalk.payment.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.ddokdoghotdog.gowalk.payment.dto.ShopApproveRequestDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderItemDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderRequestDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopReadyRequestDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopReadyResponseDTO;

@Service
public class KakaoPayService {

	@Value("${kakaopay.api.secret.key}")
    private String kakaopaySecretKey;

    @Value("${cid}")
    private String cid;

    @Value("${sample.host}")
    private String sampleHost;

    private String tid;
    private String userId;
    private String orderId;
	private ShopOrderRequestDTO shopOrderRequestDTO;
	
	@Autowired
	private PaymentSychService paymentSychService;
	
	@Autowired
	private PaymentParentService paymentParentService;

    public ShopReadyResponseDTO ready(String agent, String openType, 
    									ShopOrderRequestDTO shopOrderRequestDTO, Long memberId) {
        this.userId = String.valueOf(memberId);
        this.shopOrderRequestDTO = shopOrderRequestDTO;
    	
    	// Request header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<ShopOrderItemDTO> orderItems = shopOrderRequestDTO.getOrderItems();
        
        this.orderId = UUID.randomUUID().toString().replaceAll("-","");
        
        // Request param
        ShopReadyRequestDTO readyRequest = ShopReadyRequestDTO.builder()
                .cid(cid)
                .partnerOrderId(orderId)
                .partnerUserId(userId)
                .itemName(orderItems.size() == 1 ? 
                			orderItems.get(0).getProductName() : 
                			orderItems.get(0).getProductName() + " 외 " + orderItems.size() + "개")
                .quantity(1)
                .totalAmount(shopOrderRequestDTO.getTotalAmount())
                .taxFreeAmount(0)
                .vatAmount(0)
                .approvalUrl(sampleHost + "/api/shop/payments/approve/" + agent + "/" + openType)
                .cancelUrl(sampleHost + "/api/shop/payments/cancel/" + agent + "/" + openType)
                .failUrl(sampleHost + "/api/shop/payments/fail/" + agent + "/" + openType)
                .build();

        // Send reqeust
        HttpEntity<ShopReadyRequestDTO> entityMap = new HttpEntity<>(readyRequest, headers);
        ResponseEntity<ShopReadyResponseDTO> response = new RestTemplate().postForEntity(
                "https://open-api.kakaopay.com/online/v1/payment/ready",
                entityMap,
                ShopReadyResponseDTO.class
        );
        ShopReadyResponseDTO readyResponse = response.getBody();

        // 주문번호와 TID를 매핑해서 저장해놓는다.
        // Mapping TID with partner_order_id then save it to use for approval request.
        this.tid = readyResponse.getTid();
        return readyResponse;
    }

    
    public String approve(String pgToken) {
        // ready할 때 저장해놓은 TID로 승인 요청
        // Call “Execute approved payment” API by pg_token, TID mapping to the current payment transaction and other parameters.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request param
        ShopApproveRequestDTO approveRequest = ShopApproveRequestDTO.builder()
                .cid(cid)
                .tid(tid)
                .partnerOrderId(orderId)
                .partnerUserId(userId)
                .pgToken(pgToken)
                .build();

        // Send Request
        HttpEntity<ShopApproveRequestDTO> entityMap = new HttpEntity<>(approveRequest, headers);
        try {
        	        	
            ResponseEntity<String> response = new RestTemplate().postForEntity(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    entityMap,
                    String.class
            );

            // 승인 결과를 저장한다.
        	// save the result of approval
        	// approve전에 Order테이블, OrderItem테이블, Payment테이블 insert
        	// approve후에 장바구니 테이블 업데이트, 멤버 테이블의 포인트 업데이트
        	paymentParentService.processProduct(shopOrderRequestDTO, Long.parseLong(userId), orderId, tid);

            
            String approveResponse = response.getBody();
            return approveResponse;
        } catch (HttpStatusCodeException ex) {
            return ex.getResponseBodyAsString();
        } catch (Exception e) {
        	e.printStackTrace();
        	return "결제 중 오류가 발생했습니다.";
        }
    }
}
