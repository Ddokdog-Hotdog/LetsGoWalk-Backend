package com.ddokdoghotdog.gowalk.post.dto.request;

import java.sql.Timestamp;

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
public class PostDeteailUpdateRequestDTO {
	
	private Long postid;
	private String title;
	private String content;
	private Timestamp updatedAt;
	private String imgUrl;
	
}
