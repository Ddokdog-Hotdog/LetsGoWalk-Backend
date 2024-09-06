package com.ddokdoghotdog.gowalk.member.dto;

import java.util.Date;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDTO {
	private String nickname;
    private String profileImageUrl;
    private Date dateOfBirth;
    private String gender;
    
    @Pattern(regexp = "^\\d{2,3}\\d{3,4}\\d{4}$", message = "전화번호 형식에 맞지 않습니다.")
    private String phoneNumber;
}