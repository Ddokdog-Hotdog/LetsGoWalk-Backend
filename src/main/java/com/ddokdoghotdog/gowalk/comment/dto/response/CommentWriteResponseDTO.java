package com.ddokdoghotdog.gowalk.comment.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentWriteResponseDTO {

	private Long postid;
	private Long memberid;
    private Long commentid;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    
}
