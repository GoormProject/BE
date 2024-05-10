package com.ttokttak.jellydiary.sns.service;

import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;

public interface SnsService {
    ResponseDto<?> getSnsList(CustomOAuth2User customOAuth2User);
}
