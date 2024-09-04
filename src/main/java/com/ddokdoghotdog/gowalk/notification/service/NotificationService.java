package com.ddokdoghotdog.gowalk.notification.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationService {
	
	private final RabbitTemplate rabbitTemplate;
	
	public void sendNotification(String message) {
		rabbitTemplate.convertAndSend("commentNotification", "comment.notification", message);
	}
}
