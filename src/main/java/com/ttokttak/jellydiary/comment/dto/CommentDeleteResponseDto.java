package com.ttokttak.jellydiary.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentDeleteResponseDto {
    private Long commentId;
    private Boolean isDeleted;

    @Builder
    public CommentDeleteResponseDto(Long commentId, Boolean isDeleted) {
        this.commentId = commentId;
        this.isDeleted = isDeleted;
    }
}
