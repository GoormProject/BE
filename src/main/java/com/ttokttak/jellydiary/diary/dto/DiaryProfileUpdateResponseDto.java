package com.ttokttak.jellydiary.diary.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryProfileUpdateResponseDto {

    private Long diaryId;
    private String diaryName;
    private String diaryDescription;
    
}
