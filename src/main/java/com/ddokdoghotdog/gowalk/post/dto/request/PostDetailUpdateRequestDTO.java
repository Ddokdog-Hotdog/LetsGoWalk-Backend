package com.ddokdoghotdog.gowalk.post.dto.request;

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
public class PostDetailUpdateRequestDTO {
	
	private Long postid;
    private Long boardid;
    private String title;
    private String contents;
    private LocalDateTime updatedAt;
    
}
