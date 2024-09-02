package com.ddokdoghotdog.gowalk.comment.service;

import org.springframework.stereotype.Service;

import com.ddokdoghotdog.gowalk.auth.repository.MemberRepository;
import com.ddokdoghotdog.gowalk.comment.dto.request.CommentDeleteRequestDTO;
import com.ddokdoghotdog.gowalk.comment.dto.request.CommentEditRequestDTO;
import com.ddokdoghotdog.gowalk.comment.dto.request.CommentWriteRequestDTO;
import com.ddokdoghotdog.gowalk.comment.repository.CommentRepository;
import com.ddokdoghotdog.gowalk.entity.Comment;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Post;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.post.repository.PostRepository;


//import io.swagger.v3.oas.annotations.Operation;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {

	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	
	// 댓글 작성
	public Comment createComment(CommentWriteRequestDTO dto, Long memberId) {
		
		Post post = postRepository.findByIdAndMemberId(dto.getPostid(), memberId)
	            .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
		
		Comment parentComment = null;
		if (dto.getCommentsid() != null) {
			parentComment = commentRepository.findById(dto.getCommentsid())
					.orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
		}

		Comment comment = Comment.builder()
				.member(post.getMember())
				.post(post)
				.contents(dto.getContents())
				.parentComment(parentComment)
				.build();

		commentRepository.save(comment);

		return comment;
	}

	//댓글 수정
	public void editComment(Long commentid, CommentEditRequestDTO dto, Long memberId) {
		
		Comment comment = commentRepository.findById(commentid)
				.orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
		
	    if (!comment.getMember().getId().equals(memberId)) {
	        throw new BusinessException(ErrorCode.NO_PERMISSION);
	    }
		
		comment.setContents(dto.getContents());
		commentRepository.save(comment);
	}

	// 댓글 삭제
	public void deleteComment(Long commentid, CommentDeleteRequestDTO dto, Long memberId) {
		
		Comment comment = commentRepository.findById(commentid)
				.orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
		
	    if (!comment.getMember().getId().equals(memberId)) {
	        throw new BusinessException(ErrorCode.NO_PERMISSION);
	    }
		commentRepository.delete(comment);
	}
	
}
