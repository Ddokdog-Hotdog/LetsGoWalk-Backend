package com.ddokdoghotdog.gowalk.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

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
public class PostGetDetailResponseDTO {

	private Long postid;
    private Long boardid;
    private String title;
    private String contents;
    private String profileImageUrl;
    private String nickname;
    private Long memberid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likesCount;
    private int commentsCount;
    private List<String> imgList;
    private List<CommentDTO> comments;
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDTO {
    	
        private Long id;
        private Long commentsId;
        private String nickname;
        private String content;
        private String profileImg;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

    }

}
