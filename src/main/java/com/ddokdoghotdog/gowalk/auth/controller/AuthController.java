package com.ddokdoghotdog.gowalk.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.global.jwt.TokenProvider;
import com.ddokdoghotdog.gowalk.global.jwt.TokenService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final TokenService tokenService;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
  

    @GetMapping("/success")
    public ResponseEntity<String> loginSuccess(@RequestParam("accessToken") String accessToken) {
        return ResponseEntity.ok(accessToken);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
        tokenService.deleteRefreshToken(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

}
