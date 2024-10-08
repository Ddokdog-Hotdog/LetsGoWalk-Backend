package com.ddokdoghotdog.gowalk.global.security;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.auth.dto.PrincipalDetails;
import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.auth.repository.UserRoleRepository;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.UserRole;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

        private final MemberRepository memberRepository;
        private final UserRoleRepository userRoleRepository;

        @Transactional
        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                // 유저 정보(attributes) 가져오기
                Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

                String registrationId = userRequest.getClientRegistration().getRegistrationId();

                // userNameAttributeName 가져오기
                String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                .getUserInfoEndpoint().getUserNameAttributeName();
                // oAuth2UserInfo 변환
                OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId,
                                oAuth2UserAttributes);

                // 회원가입 및 로그인
                Member member = getOrSave(oAuth2UserInfo);

                // OAuth2User로 반환
                return new PrincipalDetails(member, oAuth2UserAttributes,
                                userNameAttributeName);
        }

        private Member getOrSave(OAuth2UserInfo oAuth2UserInfo) {
                String email = oAuth2UserInfo.getEmail();
                String provider = oAuth2UserInfo.getProvider();
                Member member = memberRepository.findByEmailAndSocialProvider(email, provider)
                                .orElseGet(() -> createNewMember(oAuth2UserInfo));
                return memberRepository.save(member);
        }

        private Member createNewMember(OAuth2UserInfo oAuth2UserInfo) {
                UserRole roleUser = userRoleRepository.findByRole("ROLE_USER")
                                .orElseThrow(() -> new IllegalArgumentException("ROLE_USER가 존재하지 않습니다."));

                Member newMember = Member.builder()
                                //.nickname(oAuth2UserInfo.getNickname())
                                .email(oAuth2UserInfo.getEmail())
                                .socialProvider(oAuth2UserInfo.getProvider())
                                .profileImageUrl(oAuth2UserInfo.getProfile())
                                .role(roleUser)
                                .memberKey(UUID.randomUUID().toString().replace("-", ""))
                                .point(0L)
                                .build();

                return memberRepository.save(newMember);
        }
}
