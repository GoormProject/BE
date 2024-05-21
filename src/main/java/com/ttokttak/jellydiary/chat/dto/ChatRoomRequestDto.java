package com.ttokttak.jellydiary.chat.dto;

import lombok.Getter;

@Getter
public class ChatRoomRequestDto {

    private Long diaryId;

    private Long userId;

    private String chatRoomType;

}
