package com.ttokttak.jellydiary.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatMessageListResponseDto {

    private List<ChatMessageResponseDto> chatMessageList;

    private boolean hasPrevious;

    private boolean hasNext;

    private int page;

    private Boolean isDiaryDeleted;

    @Builder
    public ChatMessageListResponseDto(List<ChatMessageResponseDto> chatMessageList, boolean hasPrevious, boolean hasNext, int page) {
        this.chatMessageList = chatMessageList;
        this.hasPrevious = hasPrevious;
        this.hasNext = hasNext;
        this.page = page;
    }

    public void isDiaryDeleted(Boolean isDiaryDeleted) {
        this.isDiaryDeleted = isDiaryDeleted;
    }
}
