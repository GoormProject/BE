package com.ttokttak.jellydiary.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReplyCommentGetListResponseDto {

    private Long commentId;
    private List<CommentCreateCommentInfoDto> replies;

    @Builder
    public ReplyCommentGetListResponseDto(Long commentId, List<CommentCreateCommentInfoDto> replies) {
        this.commentId = commentId;
        this.replies = replies;
    }
}
