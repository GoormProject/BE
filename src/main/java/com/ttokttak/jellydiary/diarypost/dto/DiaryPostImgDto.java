package com.ttokttak.jellydiary.diarypost.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryPostImgDto {
    private Long postImgId;
    private String imageLink;
    private Long postId;
}
