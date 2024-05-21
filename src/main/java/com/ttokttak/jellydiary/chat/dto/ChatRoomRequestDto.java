package com.ttokttak.jellydiary.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomRequestDto {

    private Long diaryId;

    private Long userId;

    private String chatRoomType;

}
