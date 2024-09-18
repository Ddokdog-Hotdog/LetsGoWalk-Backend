package com.ddokdoghotdog.gowalk.notification.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddokdoghotdog.gowalk.notification.dto.NotificationDTO;
import com.ddokdoghotdog.gowalk.notification.service.NotificationService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotificationsForUser(Principal principal) {
		Long memberId = Long.parseLong(principal.getName());

    	List<NotificationDTO> notifications = notificationService.findAllNotificationsByUserId(memberId);
        return ResponseEntity.ok(notifications);
    }
    
    // 모든 알림을 읽음으로 표시하는 엔드포인트
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllNotificationsAsRead(Principal principal) {
		Long memberId = Long.parseLong(principal.getName());

        notificationService.markAllAsReadByUserId(memberId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkNewNotifications(Principal principal) {
		Long memberId = Long.parseLong(principal.getName());

        boolean hasUnread = notificationService.checkUnreadNotifications(memberId);
        return ResponseEntity.ok(hasUnread);
    }
}