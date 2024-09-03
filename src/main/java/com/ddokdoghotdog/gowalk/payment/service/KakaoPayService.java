package com.ddokdoghotdog.gowalk.payment.service;

import java.util.List;
import java.util.Map;
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

import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.payment.dto.ShopApproveRequestDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopCancelRequestDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderItemDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderRequestDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopReadyRequestDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopReadyResponseDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopRefundRequestDTO;
import com.ddokdoghotdog.gowalk.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
	
	private final String successRedirectURL = "http://frontendURL";
	
	@Autowired
	private PaymentSychService paymentSychService;
	
	@Autowired
	private PaymentParentService paymentParentService;
	
	@Autowired
	private ProductService productService;

	@Transactional
    public ShopReadyResponseDTO ready(String agent, String openType, 
    									ShopOrderRequestDTO shopOrderRequestDTO, Long memberId) {
        this.userId = String.valueOf(memberId);
        this.shopOrderRequestDTO = shopOrderRequestDTO;
    	
    	// Request header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 여기서 shopOrderRequestDTO에 대한 검증이 필요함 가격에 장난을 치지 않았는지
        // 실제 요청이 온 것에 대한 productId에 대한 가격과 DB에 있는 가격을 비교해서 총 가격이 맞는지 확인한다.
        productService.inspectShopOrderRequestDTO(shopOrderRequestDTO);
        
        List<ShopOrderItemDTO> orderItems = shopOrderRequestDTO.getOrderItems();
        
        this.orderId = UUID.randomUUID().toString().replaceAll("-","");
        
        // Request param
        ShopReadyRequestDTO readyRequest = ShopReadyRequestDTO.builder()
                .cid(cid)
                .partnerOrderId(orderId)
                .partnerUserId(userId)
                .itemName(orderItems.size() == 1 ? 
                			orderItems.get(0).getProductName() : 
                			orderItems.get(0).getProductName() + " 등 " + orderItems.size() + "개")
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

    @Transactional
    public String approve(String pgToken) {
        // ready할 때 저장해놓은 TID로 승인 요청
        // Call “Execute approved payment” API by pg_token, TID mapping to the current payment transaction and other parameters.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request param
        ShopApproveRequestDTO approveRequest = ShopApproveRequestDTO.builder()
                .cid(cid)
//                .tid("0") // 결제 실패 유도 테스트
                .tid(tid)
                .partnerOrderId(orderId)
                .partnerUserId(userId)
                .pgToken(pgToken)
                .build();

        // Send Request
        HttpEntity<ShopApproveRequestDTO> entityMap = new HttpEntity<>(approveRequest, headers);
        try {
        	        
        	// 승인 결과를 저장한다.
        	// save the result of approval
        	// approve전에 Order테이블, OrderItem테이블, Payment테이블 insert
        	// approve후에 장바구니 테이블 업데이트, 멤버 테이블의 포인트 업데이트
        	paymentParentService.processProduct(shopOrderRequestDTO, Long.parseLong(userId), orderId, tid);
        	

            ResponseEntity<String> response = new RestTemplate().postForEntity(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    entityMap,
                    String.class
            );

            String approveResponse = response.getBody();

            return successRedirectURL;
        } 
        catch (HttpStatusCodeException ex) {
        	log.info("결제 실패 내용 : {}", ex.getResponseBodyAsString());
        	ex.printStackTrace();
        	throw new BusinessException(ErrorCode.PAYMENT_ERROR);
//            return ex.getResponseBodyAsString();
        } 
        catch (Exception e) {
        	e.printStackTrace();
        	throw new BusinessException(ErrorCode.PAYMENT_ERROR);
        }
    }
    
    
    @Transactional
    public void refund(ShopRefundRequestDTO shopRefundRequestDTO, Long memberId) {
    	
    	try {
    		ShopCancelRequestDTO cancelRequest = paymentParentService.refund(shopRefundRequestDTO, memberId);
    		log.info("환불 내용 : {}", cancelRequest.toString());
	    	
	    	// 8. 카카오페이 api에 취소 요청
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Authorization", "DEV_SECRET_KEY " + kakaopaySecretKey);
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	    	// Send Request
	        HttpEntity<ShopCancelRequestDTO> entityMap = new HttpEntity<>(cancelRequest, headers);
        
            ResponseEntity<String> response = new RestTemplate().postForEntity(
                    "https://open-api.kakaopay.com/online/v1/payment/cancel",
                    entityMap,
                    String.class
            );
            
            String cancelResponse = response.getBody();
        	
        } catch (HttpStatusCodeException ex) {
        	log.info("환불 실패 내용 : {}", ex.getResponseBodyAsString());
        	ex.printStackTrace();
        	throw new BusinessException(ErrorCode.REFUND_ERROR);
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new BusinessException(ErrorCode.REFUND_ERROR);
        }
    	
    }
    
    public void cancelAndFail() {
    	throw new BusinessException(ErrorCode.PAYMENT_ERROR);
    }
}
