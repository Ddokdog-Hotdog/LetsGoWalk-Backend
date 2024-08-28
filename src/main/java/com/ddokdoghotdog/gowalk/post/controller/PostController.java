package com.ddokdoghotdog.gowalk.post.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.entity.Post;
import com.ddokdoghotdog.gowalk.post.dto.request.PostWriteRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.response.PostGetDetailResponseDTO;
import com.ddokdoghotdog.gowalk.post.dto.response.PostGetListResponseDTO;
import com.ddokdoghotdog.gowalk.post.service.PostService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("post")
@AllArgsConstructor
public class PostController {
	
	private final PostService postService;


    @GetMapping
    public ResponseEntity<List<PostGetListResponseDTO>> getPosts() {
        List<PostGetListResponseDTO> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostGetDetailResponseDTO> getPostDetails(@PathVariable Long postId) {
        PostGetDetailResponseDTO postDetail = postService.getPostDetails(postId);
        return ResponseEntity.ok(postDetail);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
    
}