package com.ttokttak.jellydiary.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentGetListResponseDto {

    private Long postId;
    private List<CommentGetCommentInfoDto> comments;

    @Builder
    public CommentGetListResponseDto(Long postId, List<CommentGetCommentInfoDto> comments) {
        this.postId = postId;
        this.comments = comments;
    }
}
