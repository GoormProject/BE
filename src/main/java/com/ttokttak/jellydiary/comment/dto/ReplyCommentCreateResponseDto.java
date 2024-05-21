package com.ttokttak.jellydiary.comment.dto;

import lombok.Getter;

@Getter
public class ReplyCommentCreateResponseDto {
    private Long parentId;
    private CommentCreateCommentInfoDto reply;

    public ReplyCommentCreateResponseDto(Long parentId, CommentCreateCommentInfoDto reply) {
        this.parentId = parentId;
        this.reply = reply;
    }
}
