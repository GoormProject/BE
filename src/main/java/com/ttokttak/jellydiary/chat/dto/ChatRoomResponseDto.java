package com.ttokttak.jellydiary.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponseDto {

    private Long chatRoomId;

    private String chatRoomName;

    private String chatRoomProfileImg;

    private String chatMessagePreview;

    private LocalDateTime createdAt;

    private Long userId;

    private Long diaryId;

    private String chatRoomType;

    private boolean isDiaryDeleted;

    private Long chatUserCount;

}
