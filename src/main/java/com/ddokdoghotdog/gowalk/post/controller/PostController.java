package com.ddokdoghotdog.gowalk.post.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.entity.MediaUrlList;
import com.ddokdoghotdog.gowalk.entity.Post;
import com.ddokdoghotdog.gowalk.post.dto.request.PostDetailDeleteRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.request.PostDetailUpdateRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.request.PostWriteRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.response.PostGetDetailResponseDTO;
import com.ddokdoghotdog.gowalk.post.dto.response.PostGetListResponseDTO;
import com.ddokdoghotdog.gowalk.post.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("board/{boardid}")
    public ResponseEntity<List<PostGetListResponseDTO>> getPostList(@PathVariable("boardid") Long boardid) {
        List<PostGetListResponseDTO> post = postService.getPostList(boardid);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/{postid}")
    public ResponseEntity<PostGetDetailResponseDTO> getPostDetail(@PathVariable("postid") Long postid) {
        PostGetDetailResponseDTO postGetDetailResponseDTO = postService.getPostDetails(postid);
        return ResponseEntity.ok(postGetDetailResponseDTO);
    }

    @PostMapping("/write")
    public ResponseEntity<PostGetDetailResponseDTO> creaetPost(@RequestBody PostWriteRequestDTO dto) {

        Post post = postService.createPost(dto);

        List<String> imageUrls = post.getMediaUrls().stream()
                .map(MediaUrlList::getMediaUrl)
                .collect(Collectors.toList());

        PostGetDetailResponseDTO responseDTO = PostGetDetailResponseDTO.builder()
                .title(post.getTitle())
                .contents(post.getContents())
                .nickname(post.getMember().getNickname())
                .createdAt(post.getCreatedAt())
                .likesCount(0)
                .commentsCount(0)
                .imgList(imageUrls)
                .comments(null)
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{postid}")
    public ResponseEntity<?> editPost(@PathVariable("postid") Long postid,
            @RequestBody PostDetailUpdateRequestDTO requestDTO) {

        postService.editPost(postid, requestDTO);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postid}")
    public ResponseEntity<Void> deletePost(@PathVariable("postid") Long postid,
            @RequestBody PostDetailDeleteRequestDTO dto) {
        postService.deletePost(postid, dto);
        return ResponseEntity.ok().build();
    }

}