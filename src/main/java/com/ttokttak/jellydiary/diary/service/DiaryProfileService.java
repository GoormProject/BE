package com.ttokttak.jellydiary.diary.service;

import com.ttokttak.jellydiary.diary.dto.DiaryProfileRequestDto;
import com.ttokttak.jellydiary.diary.dto.DiaryProfileUpdateRequestDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;

public interface DiaryProfileService {

    ResponseDto<?> createDiaryProfile(DiaryProfileRequestDto diaryProfileRequestDto, CustomOAuth2User customOAuth2User );

    ResponseDto<?> updateDiaryProfile(Long diaryId, DiaryProfileUpdateRequestDto diaryProfileUpdateRequestDto, CustomOAuth2User customOAuth2User);

    ResponseDto<?> getDiaryProfileInfo(Long diaryId);

    ResponseDto<?> getMySubscribedOrParticipatingDiariesList(CustomOAuth2User customOAuth2User);

}
