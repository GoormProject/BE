package com.ttokttak.jellydiary.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatMessageResponseDto {

    private Long chatMessageId;

    private String chatMessage;

    private LocalDateTime createdAt;

    private String chatRoomId;

    private String userId;

    private String userName;

    private String userProfileImg;

}
