package com.ttokttak.jellydiary.diary.service;

import com.ttokttak.jellydiary.diary.dto.DiaryUserUpdateRequestDto;
import com.ttokttak.jellydiary.util.dto.ResponseDto;

import java.util.List;

public interface DiaryUserService {

    ResponseDto<?> getDiaryParticipantsList(Long diaryId);

}
