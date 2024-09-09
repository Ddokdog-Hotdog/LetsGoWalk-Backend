package com.ddokdoghotdog.gowalk.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ddokdoghotdog.gowalk.global.annotation.RequiredMemberId;
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderRequestDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopReadyResponseDTO;
import com.ddokdoghotdog.gowalk.payment.dto.ShopRefundRequestDTO;
import com.ddokdoghotdog.gowalk.payment.service.KakaoPayService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("api/shop/payments")
public class PaymentController {

	@Autowired
	private KakaoPayService kakaoPayService;
	
	@Value("${custom.payment.successfulURL}")
	private String successfulURL;
	
	@Value("${custom.payment.cancelURL}")
	private String cancelURL;
	
	@Value("${custom.payment.failURL}")
	private String failURL;
	
	@ResponseBody
	@RequiredMemberId
	@PostMapping("/ready/{agent}/{openType}")
    public ResponseEntity<?> ready(@PathVariable("agent") String agent, @PathVariable("openType") String openType,
    					@RequestBody ShopOrderRequestDTO shopOrderRequestDTO, Long memberId) {
		
		// 테스트용 - 시작
//		List<ShopOrderItemDTO> orderItems = new ArrayList<>();
		
		// 장바구니에 담지 않는 단건 결제 테스트용
//		orderItems.add(ShopOrderItemDTO.builder()
//						.cartItemId(0L) // 만약 장바구니에 담지 않고 바로 단건 결제하는 경우 cartItemId에 0L을 넣는다.
//						.productId(2L)
//						.productName("Sample Product2")
//						.quantity(1L)
//						.build());
		
		// 장바구니에 담는 단건 결제 테스트용
//		orderItems.add(ShopOrderItemDTO.builder()
//				.cartItemId(24L) 
//				.productId(4L)
//				.productName("Sample Product4")
//				.quantity(1L)
//				.build());
		
		// 장바구니에 담는 여러건 결제 테스트용
//		orderItems.add(ShopOrderItemDTO.builder()
//				.cartItemId(23L) 
//				.productId(3L)
//				.productName("Sample Product3")
//				.quantity(1L)
//				.build());
//		orderItems.add(ShopOrderItemDTO.builder()
//				.cartItemId(24L) 
//				.productId(4L)
//				.productName("Sample Product4")
//				.quantity(1L)
//				.build());
		
//		ShopOrderRequestDTO testRequest = ShopOrderRequestDTO.builder()
//							.orderItems(orderItems)
//							.totalAmount(10000)
//							.address("서울")
//							.point(0L)
//							.build();
//		ShopReadyResponseDTO readyResponse = kakaoPayService.ready(agent, openType, testRequest, memberId);
		// 테스트 용 - 끝
		
		// 실제 배포 시 아래 코드 주석 해제
		ShopReadyResponseDTO readyResponse = kakaoPayService.ready(agent, openType, shopOrderRequestDTO, memberId);
        if (agent.equals("mobile")) {
            // 모바일은 결제대기 화면으로 redirect 한다.
            // In mobile, redirect to payment stand-by screen
            return ResponseEntity.status(HttpStatus.OK)
					.body(readyResponse.getNext_redirect_mobile_url());
        }
        // pc
        //model.addAttribute("response", readyResponse);
        return ResponseEntity.status(HttpStatus.OK)
				.body(readyResponse.getNext_redirect_pc_url());
    }


    @GetMapping("/approve/{agent}/{openType}")
    public String approve(@PathVariable("agent") String agent, @PathVariable("openType") String openType, @RequestParam("pg_token") String pgToken) {
        System.out.println("approve 호출, pgToken값 : " + pgToken);
    	String successRedirectURL = kakaoPayService.approve(pgToken);
    	log.info("승인 후 리다이렉트 할 url : {}", successRedirectURL);
        return "redirect:" + this.successfulURL;
    }


    @GetMapping("/cancel/{agent}/{openType}")
    public String cancel(@PathVariable("agent") String agent, @PathVariable("openType") String openType) {
		log.info("결제가 취소되었습니다!!");
		return "redirect:" + this.cancelURL;
    }

	
    @GetMapping("/fail/{agent}/{openType}")
    public String fail(@PathVariable("agent") String agent, @PathVariable("openType") String openType) {
		log.info("결제가 실패되었습니다!!");
		return "redirect:" + this.failURL;
    }
	
	@RequiredMemberId
	@ResponseBody
	@PostMapping("refund")
	public ResponseEntity<?> refund(@RequestBody ShopRefundRequestDTO shopRefundRequestDTO, Long memberId){
		kakaoPayService.refund(shopRefundRequestDTO, memberId);
		return ResponseEntity.status(HttpStatus.OK)
				.body("환불완료");
	}
}
