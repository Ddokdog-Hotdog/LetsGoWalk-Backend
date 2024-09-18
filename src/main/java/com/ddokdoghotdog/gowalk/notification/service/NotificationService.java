package com.ddokdoghotdog.gowalk.notification.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ddokdoghotdog.gowalk.entity.Member;
import com.ddokdoghotdog.gowalk.entity.Notification;
import com.ddokdoghotdog.gowalk.notification.NotificationRepository;
import com.ddokdoghotdog.gowalk.notification.dto.NotificationDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationService {
	
	private final RabbitTemplate rabbitTemplate;
	private final NotificationRepository notificationRepository;
	
	public void sendNotification(NotificationDTO notificationDTO, Long recipientId) {
        rabbitTemplate.convertAndSend("commentNotificationExchange", "comment.notification." + recipientId, notificationDTO);
    }
	
	@Transactional
    public Notification saveNotification(NotificationDTO notificationDTO, Member recipient) {
        Notification notification = Notification.builder()
            .member(recipient)
            .type(notificationDTO.getType())
            .content(notificationDTO.getCommentContent())
            .createAt(new Timestamp(System.currentTimeMillis()))
            .isRead(false)
            .build();
        return notificationRepository.save(notification);
    }
	
	public List<NotificationDTO> findAllNotificationsByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByMemberId(userId);
        return notifications.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
	
	public void markAsRead(Long notificationId) {
	    Notification notification = notificationRepository.findById(notificationId)
	        .orElseThrow(() -> new RuntimeException("Notification not found"));
	    notification.setIsRead(true);
	    notificationRepository.save(notification);
	}
	
	@Transactional
    public void markAllAsReadByUserId(Long userId) {
        List<Notification> notifications = notificationRepository.findByMemberId(userId);
        for (Notification notification : notifications) {
            notification.setIsRead(true);
        }
        notificationRepository.saveAll(notifications);
    }
	
	public boolean checkUnreadNotifications(Long memberId) {
        return notificationRepository.existsByMemberIdAndIsReadFalse(memberId);
    }
	
	
	private NotificationDTO toDto(Notification notification) {
        return new NotificationDTO(
            notification.getId(),
            notification.getMember().getId(),
            notification.getMember().getNickname(),
            notification.getType(),
            notification.getContent(),
            notification.getIsRead()
        );
    }
}
