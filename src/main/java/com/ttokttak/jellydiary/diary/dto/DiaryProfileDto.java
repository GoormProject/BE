package com.ttokttak.jellydiary.diary.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryProfileDto {
    private Long diaryId;
    private String diaryName;
    private String diaryDescription;
    private String diaryProfileImage;
    private Boolean isDiaryDeleted;
    private Long chatRoomId;
}
