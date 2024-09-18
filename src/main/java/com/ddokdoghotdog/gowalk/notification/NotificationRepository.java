package com.ddokdoghotdog.gowalk.notification;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	List<Notification> findByMemberId(Long memberId);
    boolean existsByMemberIdAndIsReadFalse(Long memberId);

	
}
