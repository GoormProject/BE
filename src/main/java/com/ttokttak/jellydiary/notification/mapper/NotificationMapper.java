package com.ttokttak.jellydiary.notification.mapper;

import com.ttokttak.jellydiary.notification.dto.NotificationGetListResponseDto;
import com.ttokttak.jellydiary.notification.dto.NotificationResponseDto;
import com.ttokttak.jellydiary.notification.entity.NotificationEntity;
import com.ttokttak.jellydiary.notification.entity.NotificationType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", imports = { NotificationType.class})
public interface NotificationMapper {
    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    @Mapping(target = "notificationType", expression = "java(NotificationType.getContentType(notification))")
    @Mapping(target = "content", expression = "java(notification.getContent().getContent())")
    @Mapping(target = "receiverId", expression = "java(notification.getReceiver().getUserId())")
    NotificationResponseDto entityToNotificationResponseDto(NotificationEntity notification, Long returnId);

    NotificationGetListResponseDto dtoToNotificationGetListResponseDto(Long count, List<NotificationResponseDto> notificationResponseDtos);

}
