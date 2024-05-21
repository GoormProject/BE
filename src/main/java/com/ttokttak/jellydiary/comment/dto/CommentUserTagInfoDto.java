package com.ttokttak.jellydiary.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentUserTagInfoDto {
    private Long userId;
    private String userName;

    @Builder
    public CommentUserTagInfoDto(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
