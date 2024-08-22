package com.ddokdoghotdog.gowalk.global.jwt;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.global.exception.TokenException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public void deleteRefreshToken(String memberKey) {
        tokenRepository.deleteById(memberKey);
    }

    @Transactional
    public void saveOrUpdate(String memberKey, String refreshToken, String accessToken) {
        Token token = tokenRepository.findByAccessToken(accessToken)
                .map(o -> o.updateRefreshToken(refreshToken))
                .orElseGet(() -> new Token(memberKey, refreshToken, accessToken));

        tokenRepository.save(token);
    }

    public Token findByAccessTokenOrThrow(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new TokenException(ErrorCode.EXPIRED_TOKEN));
    }

    @Transactional
    public void updateToken(String accessToken, Token token) {
        token.updateAccessToken(accessToken);
        tokenRepository.save(token);
    }
}
