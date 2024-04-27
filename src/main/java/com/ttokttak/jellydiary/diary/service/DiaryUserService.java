package com.ttokttak.jellydiary.diary.service;

import com.ttokttak.jellydiary.diary.dto.DiaryUserRequestDto;
import com.ttokttak.jellydiary.diary.dto.DiaryUserUpdateRoleRequestDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;

import java.util.List;

public interface DiaryUserService {

    ResponseDto<?> getDiaryParticipantsList(Long diaryId);

    ResponseDto<?> createDiaryUser(DiaryUserRequestDto diaryUserRequestDto, CustomOAuth2User customOAuth2User);

    ResponseDto<?> updateDiaryParticipantsRolesList(Long diaryId, List<DiaryUserUpdateRoleRequestDto> updateRequestDtoList, CustomOAuth2User customOAuth2User);

    ResponseDto<?> updateDiaryUserIsInvited(Long diaryUserId);

    ResponseDto<?> deleteDiaryUser(Long diaryUserId, CustomOAuth2User customOAuth2User);

}
