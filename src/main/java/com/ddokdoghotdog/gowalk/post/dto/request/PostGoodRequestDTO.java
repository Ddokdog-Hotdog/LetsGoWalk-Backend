package com.ddokdoghotdog.gowalk.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostGoodRequestDTO {
	private Long memberid;
	private Long postid;
}
