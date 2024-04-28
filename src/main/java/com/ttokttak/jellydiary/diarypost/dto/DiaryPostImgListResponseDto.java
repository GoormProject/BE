package com.ttokttak.jellydiary.diarypost.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DiaryPostImgListResponseDto {
    private String diaryPostImg;

    @Builder
    public DiaryPostImgListResponseDto(String diaryPostImg) {
        this.diaryPostImg = diaryPostImg;
    }
}
