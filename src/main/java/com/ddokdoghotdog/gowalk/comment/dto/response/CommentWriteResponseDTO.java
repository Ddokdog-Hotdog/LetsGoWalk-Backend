package com.ddokdoghotdog.gowalk.comment.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentWriteResponseDTO {

	private Long postid;
    private Long commentid;
    private String nickname;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    
}
