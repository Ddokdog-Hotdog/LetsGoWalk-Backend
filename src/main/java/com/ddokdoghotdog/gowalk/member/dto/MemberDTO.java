package com.ddokdoghotdog.gowalk.member.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
	private String nickname;
    private String profileImageUrl;
    private Long point;
    private String email;
    private Date dateOfBirth;
    private String gender;
    private String phoneNumber;
}

