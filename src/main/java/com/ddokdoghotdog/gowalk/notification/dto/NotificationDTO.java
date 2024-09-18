package com.ddokdoghotdog.gowalk.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
	private Long id;
    private Long commenterId;
    private String commenterNickname;
    private String type;
    private String commentContent;
    private boolean isRead;
    
    public NotificationDTO(Long postId, Long commenterId, String commenterNickname, String commentContent) {
        this.id = postId;
        this.commenterId = commenterId;
        this.commenterNickname = commenterNickname;
        this.commentContent = commentContent;
        this.type = "Comment";  // type은 이 경우 고정
        this.isRead = false;  // 새 알림은 기본적으로 읽지 않은 상태
    }
}
