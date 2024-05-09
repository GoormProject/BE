package com.ttokttak.jellydiary.user.service;

import com.ttokttak.jellydiary.diary.dto.DiaryProfileUpdateRequestDto;
import com.ttokttak.jellydiary.user.dto.UserNameCheckRequestDto;
import com.ttokttak.jellydiary.user.dto.UserProfileUpdateRequestDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    ResponseDto<?> getUserProflie(CustomOAuth2User customOAuth2User);

    ResponseDto<?> updateUserProfileImg(CustomOAuth2User customOAuth2User, MultipartFile newProfileImg);

    ResponseDto<?> checkUserName(CustomOAuth2User customOAuth2User, UserNameCheckRequestDto userNameCheckRequestDto);
}
