package com.ttokttak.jellydiary.notification.mapper;

import com.ttokttak.jellydiary.diarypost.mapper.DiaryPostImgMapper;
import com.ttokttak.jellydiary.notification.dto.NotificationResponseDto;
import com.ttokttak.jellydiary.notification.entity.NotificationEntity;
import com.ttokttak.jellydiary.notification.entity.NotificationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", imports = { NotificationType.class})
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);
/*
private Long notificationId;

    private String notificationType;

    private String content;

    private Long returnId;

    private Long senderId;

    private Long receiverId;

    private Boolean isRead;

    private LocalDateTime createdAt;
* */
    @Mapping(target = "notificationType", expression = "java(NotificationType.getContentType(notification))")
    @Mapping(target = "content", expression = "java(notification.getContent().getContent())")
    @Mapping(target = "receiverId", expression = "java(notification.getReceiver().getUserId())")
    NotificationResponseDto entityToNotificationResponseDto(NotificationEntity notification, Long returnId);

}
