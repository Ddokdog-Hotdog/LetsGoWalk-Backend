package com.ddokdoghotdog.gowalk.global.security;

import java.util.Map;
import java.util.UUID;

import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Role;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Builder
@Getter
@Slf4j
public class OAuth2UserInfo {
    private final String nickname;
    private final String email;
    private final String profile;
    private final String provider;

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "google" -> ofGoogle(registrationId, attributes);
            case "kakao" -> ofKakao(registrationId, attributes);
            case "naver" -> ofNaver(registrationId, attributes);
            default -> throw new BusinessException(ErrorCode.ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2UserInfo ofGoogle(String registrationId, Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .nickname((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profile((String) attributes.get("picture"))
                .provider(registrationId)
                .build();
    }

    private static OAuth2UserInfo ofKakao(String registrationId, Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .nickname((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .provider(registrationId)
                .build();
    }

    private static OAuth2UserInfo ofNaver(String registrationId, Map<String, Object> attributes) {
        // log.info("Attributes from Naver: {}", attributes);
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuth2UserInfo.builder()
                .nickname((String) response.get("nickname"))
                .email((String) response.get("email"))
                .profile((String) response.get("profile_image"))
                .provider(registrationId)
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .nickname(nickname)
                .email(email)
                .socialProvider(provider)
                .profileImageUrl(profile)
                .role(1L)
                .memberKey(UUID.randomUUID().toString().replace("-", ""))
                .build();
    }
}
