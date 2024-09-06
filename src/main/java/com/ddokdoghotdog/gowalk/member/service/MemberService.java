package com.ddokdoghotdog.gowalk.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.member.dto.MemberDTO;
import com.ddokdoghotdog.gowalk.member.dto.MemberUpdateDTO;
import com.ddokdoghotdog.gowalk.member.dto.MypageDTO;
import com.ddokdoghotdog.gowalk.pet.dto.PetDTO.Response;
import com.ddokdoghotdog.gowalk.pet.repository.PetRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberService {
	@Autowired
	private final MemberRepository memberRepository;
    @Autowired
    private final PetRepository petRepository;

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    }
    
    public MemberDTO getMemberInfo(Long memberId) {
    	Member member = memberRepository.findById(memberId)
    	        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    	MemberDTO memberInfo = MemberDTO.builder()
    	        .nickname(member.getNickname())
    	        .profileImageUrl(member.getProfileImageUrl())
    	        .point(member.getPoint())
    	        .email(member.getEmail())
    	        .dateOfBirth(member.getDateOfBirth())
    	        .gender(member.getGender())
    	        .phoneNumber(member.getPhoneNumber())
    	        .build();
    	return memberInfo;
    }
    
    public MemberDTO updateMemberInfo(Long memberId, MemberUpdateDTO updateDTO) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        System.out.println("4");
        member.setNickname(updateDTO.getNickname());
        member.setProfileImageUrl(updateDTO.getProfileImageUrl());
        member.setDateOfBirth(updateDTO.getDateOfBirth());
        member.setGender(updateDTO.getGender());
        member.setPhoneNumber(updateDTO.getPhoneNumber());
        System.out.println("5");
        memberRepository.save(member);
        System.out.println("6");
        return MemberDTO.builder()
            .nickname(member.getNickname())
            .profileImageUrl(member.getProfileImageUrl())
            .point(member.getPoint())
            .email(member.getEmail())
            .dateOfBirth(member.getDateOfBirth())
            .gender(member.getGender())
            .phoneNumber(member.getPhoneNumber())
            .build();
    }
    
    public MypageDTO getMyPageInfo(Long memberId) {
    	Member member = memberRepository.findById(memberId)
    	        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
    	MemberDTO memberInfo = MemberDTO.builder()
    	        .nickname(member.getNickname())
    	        .profileImageUrl(member.getProfileImageUrl())
    	        .point(member.getPoint())
    	        .email(member.getEmail())
    	        .dateOfBirth(member.getDateOfBirth())
    	        .gender(member.getGender())
    	        .phoneNumber(member.getPhoneNumber())
    	        .build();

    	    List<Response> petInfoList = petRepository.findByMemberId(memberId)
    	        .stream()
    	        .map(pet -> Response.builder()
    	            .name(pet.getName())
    	            .profileImageUrl(pet.getProfileImageUrl())
    	            .petId(pet.getId())
    	            .build())
    	        .collect(Collectors.toList());

    	    return MypageDTO.builder()
    	        .memberInfo(memberInfo)
    	        .pets(petInfoList)
    	        .build();
    }
}
