package com.ddokdoghotdog.gowalk.payment.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ddokdoghotdog.gowalk.payment.dto.ShopOrderItemDTO;
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
	
	@RequiredMemberId
	@GetMapping("/ready/{agent}/{openType}")
    public String ready(@PathVariable("agent") String agent, @PathVariable("openType") String openType,
    					@RequestBody(required = false) ShopOrderRequestDTO shopOrderRequestDTO, Long memberId) {
		
		
		// 테스트용
		List<ShopOrderItemDTO> orderItems = new ArrayList<>();
		orderItems.add(ShopOrderItemDTO.builder()
						.cartItemId(1L)
						.productId(1L)
						.productName("Sample 상품")
						.quantity(1L)
						.build());
		ShopOrderRequestDTO testRequest = ShopOrderRequestDTO.builder()
							.orderItems(orderItems)
							.totalAmount(10000)
							.address("서울")
							.point(0L)
							.build();
		ShopReadyResponseDTO readyResponse = kakaoPayService.ready(agent, openType, testRequest, memberId);
		
//		ShopReadyResponseDTO readyResponse = kakaoPayService.ready(agent, openType, shopOrderRequestDTO, memberId);
        if (agent.equals("mobile")) {
            // 모바일은 결제대기 화면으로 redirect 한다.
            // In mobile, redirect to payment stand-by screen
            return "redirect:" + readyResponse.getNext_redirect_mobile_url();
        }
        // pc
        //model.addAttribute("response", readyResponse);
        return "redirect:" + readyResponse.getNext_redirect_pc_url();
    }

	@ResponseBody
    @GetMapping("/approve/{agent}/{openType}")
    public ResponseEntity<?> approve(@PathVariable("agent") String agent, @PathVariable("openType") String openType, @RequestParam("pg_token") String pgToken) {
        System.out.println("approve 호출, pgToken값 : " + pgToken);
    	String successRedirectURL = kakaoPayService.approve(pgToken);
    	log.info("승인 후 리다이렉트 할 url : {}", successRedirectURL);
        return ResponseEntity.status(HttpStatus.OK)
        					.body(successRedirectURL);
    }

	@ResponseBody
    @GetMapping("/cancel/{agent}/{openType}")
    public ResponseEntity<?> cancel(@PathVariable("agent") String agent, @PathVariable("openType") String openType) {
        // 주문건이 진짜 취소되었는지 확인 후 취소 처리
		log.info("결제가 취소되었습니다!!");
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is QUIT_PAYMENT before suspending the payment
		return ResponseEntity.status(HttpStatus.OK)
				.body("결제취소");
    }

	@ResponseBody
    @GetMapping("/fail/{agent}/{openType}")
    public ResponseEntity<?> fail(@PathVariable("agent") String agent, @PathVariable("openType") String openType) {
		log.info("결제가 실패되었습니다!!");
        // 주문건이 진짜 실패되었는지 확인 후 실패 처리
        // 결제내역조회(/v1/payment/status) api에서 status를 확인한다.
        // To prevent the unwanted request cancellation caused by attack,
        // the “show payment status” API is called and then check if the status is FAIL_PAYMENT before suspending the payment
		return ResponseEntity.status(HttpStatus.OK)
				.body("결제실패");
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
