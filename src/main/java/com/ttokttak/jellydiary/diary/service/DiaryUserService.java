package com.ttokttak.jellydiary.diary.service;

import com.ttokttak.jellydiary.diary.dto.DiaryUserRequestDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;

import java.util.List;

public interface DiaryUserService {

    ResponseDto<?> getDiaryParticipantsList(Long diaryId);

    ResponseDto<?> createDiaryUser(DiaryUserRequestDto diaryUserRequestDto, CustomOAuth2User customOAuth2User);

}
