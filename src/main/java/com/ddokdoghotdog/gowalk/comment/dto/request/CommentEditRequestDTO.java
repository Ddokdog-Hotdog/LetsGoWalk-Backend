package com.ddokdoghotdog.gowalk.comment.dto.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentEditRequestDTO {

	private Long commentid;
    private Long memberid;
    private String contents;
    private LocalDateTime updatedAt;
    
}
