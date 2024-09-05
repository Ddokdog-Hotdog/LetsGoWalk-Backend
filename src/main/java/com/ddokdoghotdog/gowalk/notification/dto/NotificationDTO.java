package com.ddokdoghotdog.gowalk.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
	private Long postId;
    private Long commenterId;
    private String commenterNickname;
    private String commentContent;
}
