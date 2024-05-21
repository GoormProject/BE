package com.ttokttak.jellydiary.diarypost.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DiaryPostListResponseDto {
    private Long postId;
    private String postTitle;
    private String weather;
    private String postDate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean isPublic;
    private Boolean isDeleted;
    private Long diaryId;
    private Long userId;

    @Builder
    public DiaryPostListResponseDto(Long postId, String postDate, String postTitle, String weather, LocalDateTime createdAt, LocalDateTime modifiedAt, Boolean isPublic, Boolean isDeleted, Long diaryId, Long userId) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.weather = weather;
        this.postDate = postDate;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isPublic = isPublic;
        this.isDeleted = isDeleted;
        this.diaryId = diaryId;
        this.userId = userId;
    }
}
