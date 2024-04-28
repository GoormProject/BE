package com.ttokttak.jellydiary.diarypost.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class DiaryPostImgListRequestDto {
    private MultipartFile DiaryPostImg;
}
