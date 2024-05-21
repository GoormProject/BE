package com.ttokttak.jellydiary.diary.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryUserUpdateRoleRequestDto {

    Long diaryUserId;
    String diaryRole;

}
