package com.ttokttak.jellydiary.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
public class CommentCreateRequestDto {
    private String commentContent;
    private Set<Long> userTag;

    @Builder
    public CommentCreateRequestDto(String commentContent, Set<Long> userTag) {
        this.commentContent = commentContent;
        this.userTag = userTag;
    }
}
