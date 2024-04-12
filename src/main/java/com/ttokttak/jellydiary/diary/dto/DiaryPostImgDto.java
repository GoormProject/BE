package com.ttokttak.jellydiary.diary.dto;

import lombok.Data;

@Data
public class DiaryPostImgDto {
    private Long postImgId;
    private String imageLink;
    private Long postId;
}
