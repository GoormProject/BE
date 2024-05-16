package com.ttokttak.jellydiary.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ChatMessageListResponseDto {

    private List<ChatMessageResponseDto> chatMessageList;

    private boolean hasPrevious;

    private boolean hasNext;

    private int page;

}
