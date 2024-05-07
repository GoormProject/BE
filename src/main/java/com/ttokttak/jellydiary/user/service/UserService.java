package com.ttokttak.jellydiary.user.service;

import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;

public interface UserService {

    ResponseDto<?> getUserProflie(CustomOAuth2User customOAuth2User);
}
