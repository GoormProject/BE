package com.ttokttak.jellydiary.diary.service;

import com.ttokttak.jellydiary.diary.dto.DiaryProfileRequestDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

public interface DiaryProfileService {

    ResponseDto<?> createDiaryProfile(DiaryProfileRequestDto diaryProfileRequestDto, CustomOAuth2User customOAuth2User );

}
