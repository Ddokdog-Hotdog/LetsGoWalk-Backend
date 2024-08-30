package com.ddokdoghotdog.gowalk.comment.dto.request;

import com.ddokdoghotdog.gowalk.post.dto.request.PostDetailDeleteRequestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentDeleteRequestDTO {
	
	private Long commentid;
	
}
