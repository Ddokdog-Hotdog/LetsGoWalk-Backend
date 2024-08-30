package com.ddokdoghotdog.gowalk.comment.dto.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentWriteRequestDTO {

	private Long memberid;
    private Long postid;
    private String contents;
    private Long commentsid; // 댓글이면 null, 답글이면 해당 댓글의 ID
    private LocalDateTime createdAt;
    
}
