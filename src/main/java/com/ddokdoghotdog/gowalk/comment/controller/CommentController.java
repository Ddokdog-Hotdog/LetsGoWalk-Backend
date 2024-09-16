package com.ddokdoghotdog.gowalk.comment.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.comment.dto.request.CommentDeleteRequestDTO;
import com.ddokdoghotdog.gowalk.comment.dto.request.CommentEditRequestDTO;
import com.ddokdoghotdog.gowalk.comment.dto.request.CommentWriteRequestDTO;
import com.ddokdoghotdog.gowalk.comment.dto.response.CommentWriteResponseDTO;
import com.ddokdoghotdog.gowalk.comment.service.CommentService;
import com.ddokdoghotdog.gowalk.entity.Comment;

import io.swagger.v3.oas.annotations.Operation;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

	private final CommentService commentService;
	private final MemberRepository memberRepository;

	@PostMapping("/write")
	@Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    public ResponseEntity<CommentWriteResponseDTO> creaetComment(@RequestBody CommentWriteRequestDTO dto, Principal principal){
    	
		Long memberId = Long.parseLong(principal.getName());
    	Comment comment = commentService.createComment(dto, memberId);
    	
    	CommentWriteResponseDTO responseDTO = CommentWriteResponseDTO.builder()
    			.postid(dto.getPostid())
                .memberid(memberId)
                .nickname(comment.getMember().getNickname())
    			.commentid(dto.getCommentsid())
    			.profileImageUrl(comment.getMember().getProfileImageUrl())
    			.createdAt(dto.getCreatedAt())
    			.build();
    	
    	return ResponseEntity.ok(responseDTO);
    }
	
	@PutMapping("/{commentid}")
	@Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
	public ResponseEntity<?> editComment(@PathVariable("commentid") Long commentid, @ModelAttribute CommentEditRequestDTO dto, Principal principal) {
		
		Long memberId = Long.parseLong(principal.getName());
		commentService.editComment(commentid, dto, memberId);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{commentid}")
	@Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
	public ResponseEntity<?> deleteComment(@PathVariable("commentid") Long commentid, @ModelAttribute CommentDeleteRequestDTO dto, Principal principal){
		
		Long memberId = Long.parseLong(principal.getName());
		commentService.deleteComment(commentid, dto, memberId);

		return ResponseEntity.ok().build();
	}
}
