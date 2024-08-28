package com.ddokdoghotdog.gowalk.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostDetailDeleteRequestDTO {

	private Long userid;
    private Long postsid;
    private Long boardsid;

}
