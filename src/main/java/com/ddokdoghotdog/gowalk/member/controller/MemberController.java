package com.ddokdoghotdog.gowalk.member.controller;

import java.sql.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.auth.dto.PrincipalDetails;
import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.global.jwt.TokenProvider;
import com.ddokdoghotdog.gowalk.global.jwt.TokenService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class MemberController {



    private final TokenService tokenService;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;


    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserDetails userDetails) {
        tokenService.deleteRefreshToken(userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mypage")
    public ResponseEntity<Member> mypage(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        Member member = principalDetails.getMember();

        return new ResponseEntity<>(member, HttpStatus.OK);
    }
    
    @GetMapping("/owner/edit")
    public ResponseEntity<Member> myInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        Member member = principalDetails.getMember();

        return new ResponseEntity<>(member, HttpStatus.OK);
    }
    
    @PostMapping("/owner/edit")
    public ResponseEntity<Member> myInfoEdit(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        Member member = principalDetails.getMember();
        
        return new ResponseEntity<>(member, HttpStatus.OK);
    }
    
    @PostMapping("/register")
    public ResponseEntity<String> completeRegistration(@RequestParam("accessToken") String accessToken,
                                                  @RequestParam String gender,
                                                  @RequestParam Date birthdate) {
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        
        Member member = principal.getMember();
        member.setGender(gender);
        member.setDateOfBirth(birthdate);

        memberRepository.save(member);

        return ResponseEntity.ok("Registration completed successfully");
    }

}
