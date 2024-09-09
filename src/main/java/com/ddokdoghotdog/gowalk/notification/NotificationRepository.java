package com.ddokdoghotdog.gowalk.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
