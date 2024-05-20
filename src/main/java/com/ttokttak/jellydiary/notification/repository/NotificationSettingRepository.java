package com.ttokttak.jellydiary.notification.repository;

import com.ttokttak.jellydiary.notification.entity.NotificationSettingEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationSettingRepository extends JpaRepository<NotificationSettingEntity, Long> {

    Optional<NotificationSettingEntity> findByUser(UserEntity user);

}
