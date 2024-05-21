package com.ttokttak.jellydiary.like.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class PostLikeGetResponseDto {
    private boolean likeState;

    @Builder
    public PostLikeGetResponseDto(boolean likeState) {
        this.likeState = likeState;
    }
}
