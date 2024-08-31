package com.ddokdoghotdog.gowalk.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentController {

	private final CommentService commentService;
	private final MemberRepository memberRepository;

	@PostMapping("/write")
	public ResponseEntity<CommentWriteResponseDTO> creaetComment(@RequestBody CommentWriteRequestDTO dto) {

		Comment comment = commentService.createComment(dto);
		Member member = memberRepository.findById(dto.getMemberid())
				.orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

		CommentWriteResponseDTO responseDTO = CommentWriteResponseDTO.builder()
				.postid(dto.getPostid())
				.commentid(dto.getCommentsid())
				.nickname(member.getNickname())
				.profileImageUrl(member.getProfileImageUrl())
				.createdAt(dto.getCreatedAt())
				.build();

		return ResponseEntity.ok(responseDTO);
	}

	@PutMapping("/{commentid}")
	public ResponseEntity<?> editComment(@PathVariable("commentid") Long commentid,
			@RequestBody CommentEditRequestDTO dto) {

		commentService.editComment(commentid, dto);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{commentid}")
	public ResponseEntity<?> deleteComment(@PathVariable("commentid") Long commentid,
			@RequestBody CommentDeleteRequestDTO dto) {

		commentService.deleteComment(commentid, dto);

		return ResponseEntity.ok().build();
	}
}
