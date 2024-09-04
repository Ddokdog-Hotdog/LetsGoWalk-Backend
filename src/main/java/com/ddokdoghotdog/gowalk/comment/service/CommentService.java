package com.ddokdoghotdog.gowalk.comment.service;

import java.sql.Timestamp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.ddokdoghotdog.gowalk.comment.dto.request.CommentDeleteRequestDTO;
import com.ddokdoghotdog.gowalk.comment.dto.request.CommentEditRequestDTO;
import com.ddokdoghotdog.gowalk.comment.dto.request.CommentWriteRequestDTO;
import com.ddokdoghotdog.gowalk.comment.repository.CommentRepository;
import com.ddokdoghotdog.gowalk.entity.Comment;
import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Notification;
import com.ddokdoghotdog.gowalk.entity.Post;
import com.ddokdoghotdog.gowalk.global.exception.BusinessException;
import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.notification.NotificationRepository;
import com.ddokdoghotdog.gowalk.notification.dto.NotificationDTO;
import com.ddokdoghotdog.gowalk.post.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
@Log4j2
public class CommentService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;
	private final NotificationRepository notificationRepository;
	
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
		
		// 게시글을 쓴 사람에게 알림 보내는 로직
		Member postOwner = post.getMember();  // 게시글 작성자
		String notificationMessage = String.format("New comment on your post '%s' by %s: %s",
		                                           post.getTitle(), comment.getMember().getNickname(), comment.getContents());

		NotificationDTO messageDTO = new NotificationDTO(
		    post.getId(),
		    comment.getMember().getId(),
		    comment.getMember().getNickname(),
		    comment.getContents()
		);

		sendNotification(messageDTO, postOwner.getId());
		saveNotification(messageDTO, postOwner);

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
	
	private void sendNotification(NotificationDTO dto, Long recipientId) {
        try {
            String notificationJson = objectMapper.writeValueAsString(dto);
            rabbitTemplate.convertAndSend("commentNotificationExchange", "comment.notification." + recipientId, notificationJson);
        } catch (Exception e) {
            System.out.println("Error sending notification: " + e.getMessage());
        }
    }
	
	private void saveNotification(NotificationDTO dto, Member recipient) {
	    Notification notification = Notification.builder()
	    		.member(recipient)
	            .type("Comment")
	            .content(dto.getCommentContent())
	            .createAt(new Timestamp(System.currentTimeMillis()))
	            .isRead(false)
	            .build();
	    notificationRepository.save(notification);
	}
	
}
