package com.ttokttak.jellydiary.diary.dto;

import lombok.Data;

@Data
public class DiaryProfileDto {
    private Long diaryId;
    private String diaryName;
    private String diaryDescription;
    private String diaryProfileImage;
    private Boolean isDiaryDeleted;
    private Long chatRoomId;
}
