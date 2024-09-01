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

	public Comment createComment(CommentWriteRequestDTO dto) {
		
		Member member = memberRepository.findById(dto.getMemberid())
		        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
		
		Post post = postRepository.findById(dto.getPostid())
				.orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
		
		Comment parentComment = null;
		if(dto.getCommentsid() != null) {
			parentComment = commentRepository.findById(dto.getCommentsid())
		            .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
		}
		
		Comment comment = Comment.builder()
				.member(member)
				.post(post)
				.contents(dto.getContents())
				.parentComment(parentComment)
				.build();
		
		commentRepository.save(comment);
		
		return comment;
	}

	public void editComment(Long commentid, CommentEditRequestDTO dto) {
		
		Comment comment = commentRepository.findById(commentid)
				.orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
		
		comment.setContents(dto.getContents());
		
		commentRepository.save(comment);
	}

	public void deleteComment(Long commentid, CommentDeleteRequestDTO dto) {
		
		Comment comment = commentRepository.findById(commentid)
				.orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
		
		commentRepository.delete(comment);
		
	}

}
