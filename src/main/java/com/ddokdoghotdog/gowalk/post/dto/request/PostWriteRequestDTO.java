package com.ddokdoghotdog.gowalk.post.dto.request;

import java.util.List;

import com.ddokdoghotdog.gowalk.entity.Board;
import com.ddokdoghotdog.gowalk.entity.MediaUrlList;
import com.ddokdoghotdog.gowalk.entity.Member;

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
public class PostWriteRequestDTO {
	
    private Long memberid;
    private Long boardid;
    private String title;
    private String contents;
    private List<String> mediaUrlList;

}