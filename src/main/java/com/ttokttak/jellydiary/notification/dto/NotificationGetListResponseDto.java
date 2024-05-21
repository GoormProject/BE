package com.ttokttak.jellydiary.notification.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NotificationGetListResponseDto {
    private Long count;
    private List<NotificationResponseDto> notificationResponseDtos;

    @Builder
    public NotificationGetListResponseDto(Long count, List<NotificationResponseDto> notificationResponseDtos) {
        this.count = count;
        this.notificationResponseDtos = notificationResponseDtos;
    }
}
