package com.ddokdoghotdog.gowalk.notification;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.ddokdoghotdog.gowalk.notification.dto.NotificationDTO;

@Component
public class NotificationMessageListener {
	
	@RabbitListener(queues = "commentNotificationQueue")
	public void receiveNotification(NotificationDTO notificationDTO) {
        System.out.println("Received notification: " + notificationDTO);
        //프론트로 푸시 알림 보내는 등의 추가적인 로직 구현.
	}
}
