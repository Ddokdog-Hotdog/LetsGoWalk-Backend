package com.ddokdoghotdog.gowalk.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateDTO {

	private String type;
	private String content;
	private Long recipientId;
}
