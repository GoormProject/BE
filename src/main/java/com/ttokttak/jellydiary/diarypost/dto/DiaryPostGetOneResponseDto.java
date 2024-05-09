package com.ttokttak.jellydiary.diarypost.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DiaryPostGetOneResponseDto {
    private Long postId;
    private String postDate;
    private String postTitle;
    private String meal;
    private String snack;
    private String water;
    private String walk;
    private String toiletRecord;
    private String shower;
    private String weight;
    private String specialNote;
    private String weather;
    private String postContent;
    private List<DiaryPostImgListResponseDto> postImgs = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean isPublic;
    private Boolean isDeleted;
    private Long diaryId;
    private Long userId;
    private Long likeCount;
    private Long commentCount;

    @Builder
    public DiaryPostGetOneResponseDto(Long postId, String postDate, String postTitle, String meal, String snack, String water, String walk, String toiletRecord, String shower, String weight, String specialNote, String weather, String postContent, List<DiaryPostImgListResponseDto> postImgs, LocalDateTime createdAt, LocalDateTime modifiedAt, Boolean isPublic, Boolean isDeleted, Long diaryId, Long userId, Long likeCount, Long commentCount) {
        this.postId = postId;
        this.postDate = postDate;
        this.postTitle = postTitle;
        this.meal = meal;
        this.snack = snack;
        this.water = water;
        this.walk = walk;
        this.toiletRecord = toiletRecord;
        this.shower = shower;
        this.weight = weight;
        this.specialNote = specialNote;
        this.weather = weather;
        this.postContent = postContent;
        this.postImgs = postImgs;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isPublic = isPublic;
        this.isDeleted = isDeleted;
        this.diaryId = diaryId;
        this.userId = userId;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
