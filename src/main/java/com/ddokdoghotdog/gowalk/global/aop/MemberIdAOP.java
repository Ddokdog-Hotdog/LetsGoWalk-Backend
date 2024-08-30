package com.ddokdoghotdog.gowalk.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ddokdoghotdog.gowalk.global.entity.BaseMemberIdDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
public class MemberIdAOP {

    @Around("execution(* com.ddokdoghotdog.gowalk..controller..*(..))")
    public Object setMemberId(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof BaseMemberIdDTO) {
                BaseMemberIdDTO dto = (BaseMemberIdDTO) args[i];
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.isAuthenticated()) {
                    String name = authentication.getName();
                    try {
                        Long memberId = Long.parseLong(name);
                        dto.setMemberId(memberId);
                    } catch (NumberFormatException e) {
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
