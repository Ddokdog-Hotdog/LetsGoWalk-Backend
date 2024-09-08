package com.ddokdoghotdog.gowalk.member.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.global.annotation.RequiredMemberId;
import com.ddokdoghotdog.gowalk.global.jwt.TokenProvider;
import com.ddokdoghotdog.gowalk.global.jwt.TokenService;
import com.ddokdoghotdog.gowalk.member.dto.MemberDTO;
import com.ddokdoghotdog.gowalk.member.dto.MemberUpdateDTO;
import com.ddokdoghotdog.gowalk.member.dto.MypageDTO;
import com.ddokdoghotdog.gowalk.member.dto.PointDTO;
import com.ddokdoghotdog.gowalk.member.service.MemberService;
import com.ddokdoghotdog.gowalk.member.service.PointService;
import com.ddokdoghotdog.gowalk.pet.repository.PetRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final TokenService tokenService;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;
    private final PointService pointService;
    private final PetRepository petRepository;
    
    @RequiredMemberId
    @GetMapping("/register")
    public ResponseEntity<MemberDTO> completeRegistration(Long memberId) {
    	MemberDTO memberDTO = memberService.getMemberInfo(memberId);
    	if (memberDTO == null) {
            return ResponseEntity.notFound().build();
        }
    	 return ResponseEntity.ok(memberDTO);
    }
    
    @RequiredMemberId
    @PutMapping("/register")
    public ResponseEntity<?> completeRegistration (
    		@Valid @RequestPart("memberUpdateDTO") MemberUpdateDTO memberUpdateDTO, 
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage, 
        Long memberId) {

    	System.out.println("Nickname: " + memberUpdateDTO.getNickname());
    	System.out.println("Date of Birth: " + memberUpdateDTO.getDateOfBirth());
    	System.out.println("Gender: " + memberUpdateDTO.getGender());
    	System.out.println("Phone Number: " + memberUpdateDTO.getPhoneNumber());
    	
    	if (memberId == null || memberUpdateDTO == null) {
            return ResponseEntity.badRequest().build();
        }

        // 현재 사용자의 닉네임을 제외하고 중복되는 닉네임이 있는지 확인
        boolean isDuplicateNickname = memberService.checkNicknameDuplicate(memberUpdateDTO.getNickname(), memberId);
        if (isDuplicateNickname) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("닉네임이 중복되었습니다.");
        }

        memberService.updateMemberInfo(memberId, memberUpdateDTO, profileImage);
        return ResponseEntity.status(HttpStatus.OK).body("회원정보가 성공적으로 업데이트되었습니다.");

    }

    @RequiredMemberId
    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname, Long memberId) {
        boolean isDuplicate = memberService.checkNicknameDuplicate(nickname, memberId);
        
        return ResponseEntity.ok(isDuplicate);
    }

    @RequiredMemberId
    @GetMapping("/mypage")
    public ResponseEntity<MypageDTO> getMyPage(Long memberId) {
        MypageDTO myPageDTO = memberService.getMyPageInfo(memberId);
        if (myPageDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(myPageDTO);
    }
    
    @RequiredMemberId
    @GetMapping("/owner/edit")
    public ResponseEntity<MemberDTO> myInfo(Long memberId) {
    	MemberDTO memberDTO = memberService.getMemberInfo(memberId);
    	if (memberDTO == null) {
            return ResponseEntity.notFound().build();
        }
    	 return ResponseEntity.ok(memberDTO);
    }
    
    
    @RequiredMemberId
    @PutMapping("/owner/edit")
    public ResponseEntity<MemberDTO> myInfoEdit(@Valid @RequestPart("MemberUpdateDTO") MemberUpdateDTO updateDTO,
    		@RequestPart("profileImage") MultipartFile profileImage, Long memberId) {
    	System.out.println("0");
    	if (memberId == null || updateDTO == null) {
            return ResponseEntity.badRequest().build(); // 입력 값이 null인 경우, Bad Request 응답을 반환
        }
    	
    	System.out.println("1");
        // DTO 내부의 필드 검증 로직 추가 (예: nickname이 null이거나 공백인지 체크)
        if (updateDTO.getNickname() == null || updateDTO.getNickname().trim().isEmpty()) {
        	System.out.println("2");
            return ResponseEntity.badRequest().build(); // 필수 필드가 누락된 경우, Bad Request 응답을 반환
            
        } 
    	MemberDTO updatedMember = memberService.updateMemberInfo(memberId, updateDTO,profileImage);
    	System.out.println("3");
    	return ResponseEntity.ok(updatedMember);
    }
    
    @RequiredMemberId
    @GetMapping("/points")
    public ResponseEntity<List<PointDTO>> getPoints(Long memberId) {
        List<PointDTO> points = pointService.getPointTransactions(memberId);

        return ResponseEntity.ok(points);
    }

}
