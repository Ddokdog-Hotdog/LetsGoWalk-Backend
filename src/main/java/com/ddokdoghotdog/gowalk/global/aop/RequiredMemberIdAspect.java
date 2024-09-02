package com.ddokdoghotdog.gowalk.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class RequiredMemberIdAspect {

	// 테스트 용 memberId 직접 주입
	private Long defaultId = 2L;

	@Around("@annotation(com.ddokdoghotdog.gowalk.global.annotation.RequiredMemberId)")
	public Object requiredMemberId(ProceedingJoinPoint joinPoint) throws Throwable {

		// annotation이 붙은 메소드의 파라미터 값들을 배열로 가져온다.
		Object[] args = joinPoint.getArgs();
		
		// 파라미터 값들을 순회한다.
		for (int i = 0; i < args.length; i++) {
			if (i + 1 == args.length) {
				// 파라미터 타입이 Long이어야 하며, 메소드의 맨 마지막으로 전달해야함
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if (authentication != null && authentication.isAuthenticated()) {
					String name = authentication.getName();
					try {
						// 현재 로그인 중이라면 해당 파라미터에 memberId값을 넣는다.
						Long memberId = Long.parseLong(name);
						args[i] = memberId;
					} catch (NumberFormatException e) {
						// 테스트 용 => 지금 로그인 중이 아닐때 넣는 값 (실제 배포 시 지워야함)
						args[i] = this.defaultId;
						log.info("Failed to parse memberId: " + name);
					}
				} else {
					
					log.info("Authentication is null or not authenticated");
				}
			}

		}
		return joinPoint.proceed(args);

	}
}
