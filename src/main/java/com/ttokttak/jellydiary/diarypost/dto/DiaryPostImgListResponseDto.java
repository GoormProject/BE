package com.ttokttak.jellydiary.diarypost.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DiaryPostImgListResponseDto {
    private Long imgId;
    private String diaryPostImg;

    @Builder
    public DiaryPostImgListResponseDto(Long imgId, String diaryPostImg) {
        this.imgId = imgId;
        this.diaryPostImg = diaryPostImg;
    }
}
