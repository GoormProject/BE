package com.ttokttak.jellydiary.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentGetListResponseDto {

    private Long postId;
    private List<CommentCreateCommentInfoDto> comments;

    @Builder
    public CommentGetListResponseDto(Long postId, List<CommentCreateCommentInfoDto> comments) {
        this.postId = postId;
        this.comments = comments;
    }
}
