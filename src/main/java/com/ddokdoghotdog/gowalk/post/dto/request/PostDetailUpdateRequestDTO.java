package com.ddokdoghotdog.gowalk.post.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostDetailUpdateRequestDTO {
	
	private Long postid;
    private Long boardid;
    private String title;
    private String contents;
    private LocalDateTime updatedAt;
    private String img;
    
}
