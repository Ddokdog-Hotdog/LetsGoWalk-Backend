package com.ddokdoghotdog.gowalk.post.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ddokdoghotdog.gowalk.entity.MediaUrlList;
import com.ddokdoghotdog.gowalk.entity.Post;
import com.ddokdoghotdog.gowalk.post.dto.request.PostDetailDeleteRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.request.PostDetailUpdateRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.request.PostWriteRequestDTO;
import com.ddokdoghotdog.gowalk.post.dto.response.PostGetDetailResponseDTO;
import com.ddokdoghotdog.gowalk.post.dto.response.PostGetListResponseDTO;
import com.ddokdoghotdog.gowalk.post.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/post")
@AllArgsConstructor
public class PostController {

	private final PostService postService;
	
	@GetMapping("board/{boardid}")
	@Operation(summary = "게시글 조회", description = "게시글을 조회합니다.")
    public ResponseEntity<List<PostGetListResponseDTO>> getPostList(@PathVariable("boardid") Long boardid,
    																@RequestParam(name = "page" ,defaultValue = "0") int page, 
    																@RequestParam(name = "size" ,defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		
        List<PostGetListResponseDTO> post = postService.getPostList(boardid, pageable);
        return ResponseEntity.ok(post);
    }
	
	@GetMapping("/{postid}")
	@Operation(summary = "게시글 상세 조회", description = "게시글 상세 내용을 조회합니다.")
	public ResponseEntity<PostGetDetailResponseDTO> getPostDetail(@PathVariable("postid") Long postid){
		PostGetDetailResponseDTO postGetDetailResponseDTO = postService.getPostDetails(postid);
		return ResponseEntity.ok(postGetDetailResponseDTO);
	}
    
	@PostMapping("/write")
	@Operation(summary = "게시글 등록", description = "게시글을 등록합니다.")
	public ResponseEntity<PostGetDetailResponseDTO> createPost(@ModelAttribute PostWriteRequestDTO dto, 
	                                                            @RequestPart("images") List<MultipartFile> images,
	                                                            Principal principal) {
//	    Long memberId = Long.parseLong(principal.getName()); 
	    Post post = postService.createPost(dto, images, principal);
	    
	    List<String> imageUrls = post.getMediaUrls().stream()
	            .map(MediaUrlList::getMediaUrl)
	            .collect(Collectors.toList());
	    
	    PostGetDetailResponseDTO responseDTO = PostGetDetailResponseDTO.builder()
	            .title(post.getTitle())
	            .contents(post.getContents())
	            .nickname(post.getMember().getNickname())
	            .createdAt(post.getCreatedAt())
	            .likesCount(0L)
	            .commentsCount(0)
	            .imgList(imageUrls)
	            .comments(null)
	            .build();
	    
	    return ResponseEntity.ok(responseDTO);
	}
    
    @PutMapping(value = "/{postid}", consumes = "multipart/form-data")
    @Operation(summary = "게시글 수정", description = "게시글을 수정합니다.")
    public ResponseEntity<?> editPost(@PathVariable("postid") Long postid,
    								  @RequestPart("post") @Valid PostDetailUpdateRequestDTO requestDTO,
    								  @RequestPart("images") List<MultipartFile> images,
    								  Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
    	
    	postService.editPost(postid, requestDTO, images, memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postid}")

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    public ResponseEntity<Void> deletePost(@PathVariable("postid") Long postid, @RequestBody PostDetailDeleteRequestDTO dto, Principal principal) {
        
    	Long memberId = Long.parseLong(principal.getName());
    	
    	postService.deletePost(postid, dto, memberId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/like/{postid}")
    @Operation(summary = "게시글 좋아요", description = "게시글 좋아요 관련 API입니다.")
    public ResponseEntity<?> toggleLike(@PathVariable("postid") Long postid, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        
        postService.toggleLike(postid, memberId);
        Long likeCount = postService.getLikesCount(postid);
        return ResponseEntity.ok(likeCount);
    }
}