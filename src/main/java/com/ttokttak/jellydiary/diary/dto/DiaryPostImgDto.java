package com.ttokttak.jellydiary.diary.dto;

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
