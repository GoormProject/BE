package com.ttokttak.jellydiary.notification.repository;

import com.ttokttak.jellydiary.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
