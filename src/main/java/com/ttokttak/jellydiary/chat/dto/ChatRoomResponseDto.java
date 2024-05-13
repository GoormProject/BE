package com.ttokttak.jellydiary.chat.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponseDto {

    private Long chatRoomId;

    private String chatRoomName;

    private String chatRoomProfile;

    private String chatMessagePreview;

}
