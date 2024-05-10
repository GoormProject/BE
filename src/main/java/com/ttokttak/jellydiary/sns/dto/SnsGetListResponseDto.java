package com.ttokttak.jellydiary.sns.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SnsGetListResponseDto {

    private Long userId;
    private String userName;
    private String userProfileImg;
    private Long postId;
    private Long diaryId;
    private String diaryProfileImage;
    private boolean isLike;

    @Builder
    public SnsGetListResponseDto(Long userId, String userName, String userProfileImg, Long postId, Long diaryId, String diaryProfileImage, boolean isLike) {
        this.userId = userId;
        this.userName = userName;
        this.userProfileImg = userProfileImg;
        this.postId = postId;
        this.diaryId = diaryId;
        this.diaryProfileImage = diaryProfileImage;
        this.isLike = isLike;
    }
}
