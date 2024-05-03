package com.ttokttak.jellydiary.comment.dto;

import lombok.Getter;

@Getter
public class ReplyCommentCreateResponseDto {
    private Long parentId;
    private ReplyCommentCreateCommentInfoDto reply;

    public ReplyCommentCreateResponseDto(Long parentId, ReplyCommentCreateCommentInfoDto reply) {
        this.parentId = parentId;
        this.reply = reply;
    }
}
