package com.ttokttak.jellydiary.diary.dto;

import lombok.*;

@Getter
@Setter
public class DiaryProfileResponseDto {

    private Long diaryId;
    private String diaryName;
    private String diaryDescription;
    private String diaryProfileImage;
    private Boolean isDiaryDeleted;
    private Long chatRoomId;

}
