package com.ddokdoghotdog.gowalk.post.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostGetListResponseDTO {
	
	private Long postid;
    private Long boardid;
    private String title;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likesCount;
    private Long commentsCount;
    private String img;
    
}
