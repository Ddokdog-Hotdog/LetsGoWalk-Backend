package com.ddokdoghotdog.gowalk.post.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostWriteRequestDTO {
	
	private String nickname;
    private Long boardid;
    private String title;
    private String contents;
    private List<String> mediaUrlList;

}