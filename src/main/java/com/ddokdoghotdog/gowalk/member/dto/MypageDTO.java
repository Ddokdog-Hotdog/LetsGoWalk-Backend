package com.ddokdoghotdog.gowalk.member.dto;

import java.util.List;

import com.ddokdoghotdog.gowalk.pet.dto.PetDTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MypageDTO {
	private MemberDTO memberInfo;
	private List<Response> pets;

}

