package com.ttokttak.jellydiary.util.service;

import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;

public interface SearchService {
    ResponseDto<?> getUserSearch(Long diaryId, String searchWord, CustomOAuth2User customOAuth2User);
}
