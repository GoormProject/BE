package com.ttokttak.jellydiary.notification.repository;

import com.ttokttak.jellydiary.notification.entity.NotificationEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Query("select n from NotificationEntity n where n.receiver.userId = :userId order by n.notificationId desc")
    List<NotificationEntity> findAllByUserId(@Param("userId") Long userId);

    @Query("select count(n) from NotificationEntity n where n.receiver.userId = :userId and n.isRead = false")
    Long countUnReadNotifications(@Param("userId") Long userId);
}
