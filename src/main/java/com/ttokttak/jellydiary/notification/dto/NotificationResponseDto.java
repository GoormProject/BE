package com.ttokttak.jellydiary.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationResponseDto {

    private Long notificationId;

    private String notificationType;

    private String content;

    private Long returnId;

    private Long senderId;

    private Long receiverId;

    private Boolean isRead;

    private LocalDateTime createdAt;

    @Builder
    public NotificationResponseDto(Long notificationId, String notificationType, String content, Long returnId, Long senderId, Long receiverId, Boolean isRead, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.notificationType = notificationType;
        this.content = content;
        this.returnId = returnId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}