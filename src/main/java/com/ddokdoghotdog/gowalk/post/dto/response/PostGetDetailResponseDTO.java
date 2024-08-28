package com.ddokdoghotdog.gowalk.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostGetDetailResponseDTO {
	
	private Long postid;
    private Long boardid;
    private String title;
    private String contents;
    private String profileImageUrl;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likesCount;
    private int commentsCount;
    private String img;
    private List<CommentDTO> comments;
    
    public static class CommentDTO {
    	
        private Long id;
        private Long commentsId;
        private String nickname;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }
}
