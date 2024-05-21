package com.ttokttak.jellydiary.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentCreateResponseDto {

    private Long postId;
    private CommentCreateCommentInfoDto comment;

    @Builder
    public CommentCreateResponseDto(Long postId, CommentCreateCommentInfoDto comment) {
        this.postId = postId;
        this.comment = comment;
    }
}
