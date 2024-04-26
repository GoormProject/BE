package com.ttokttak.jellydiary.diary.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryUserResponseDto {

    Long diaryUserId;
    Long diaryId;
    Long userId;
    String diaryRole;
    Boolean isInvited;

}
