package com.ddokdoghotdog.gowalk.global.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.ddokdoghotdog.gowalk.auth.dto.PrincipalDetails;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.global.jwt.TokenProvider;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    @Value("${custom.frontUrl}")
    private String baseURL;

    // private String URL = baseURL + "/auth/success";
    // private String ADDITIONAL_INFO_URL = baseURL + "/auth/register";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // accessToken, refreshToken 발급
        String accessToken = tokenProvider.generateAccessToken(authentication);
        tokenProvider.generateRefreshToken(authentication, accessToken);

        // 사용자 추가 정보가 있는지 확인
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
        String redirectUrl;
        if ((member.getDateOfBirth() != null) && (member.getGender() != null) && (member.getNickname() != null)
                && (member.getPhoneNumber() != null)) {
            // 추가 정보가 있는 경우
            String URL = baseURL + "/auth/success";
            redirectUrl = UriComponentsBuilder.fromUriString(URL)
                    .queryParam("accessToken", accessToken)
                    .build().toUriString();
        } else {
            // 추가 정보가 없는 경우 추가 정보 입력 페이지로 리다이렉트
            String ADDITIONAL_INFO_URL = baseURL + "/auth/register";
            redirectUrl = UriComponentsBuilder.fromUriString(ADDITIONAL_INFO_URL)
                    .queryParam("accessToken", accessToken)
                    .build().toUriString();
            System.out.println("Redirect URL: " + redirectUrl);
        }

        response.sendRedirect(redirectUrl);
    }
}
