package com.ttokttak.jellydiary.diary.service;

import com.ttokttak.jellydiary.diary.dto.DiaryProfileRequestDto;
import com.ttokttak.jellydiary.diary.dto.DiaryProfileUpdateRequestDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface DiaryProfileService {

    ResponseDto<?> createDiaryProfile(DiaryProfileRequestDto diaryProfileRequestDto, MultipartFile diaryProfileImage, CustomOAuth2User customOAuth2User );

    ResponseDto<?> updateDiaryProfile(Long diaryId, DiaryProfileUpdateRequestDto diaryProfileUpdateRequestDto, CustomOAuth2User customOAuth2User);

    ResponseDto<?> getDiaryProfileInfo(Long diaryId);

    ResponseDto<?> getMySubscribedOrParticipatingDiariesList(CustomOAuth2User customOAuth2User);

    ResponseDto<?> deleteDiaryProfile(Long diaryId, CustomOAuth2User customOAuth2User);

    ResponseDto<?> updateDiaryProfileImg(Long diaryId, MultipartFile diaryProfileImage, CustomOAuth2User customOAuth2User);
}
