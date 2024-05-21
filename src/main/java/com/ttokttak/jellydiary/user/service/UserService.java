package com.ttokttak.jellydiary.user.service;

import com.ttokttak.jellydiary.user.dto.UserNameCheckRequestDto;
import com.ttokttak.jellydiary.user.dto.UserNotificationSettingRequestDto;
import com.ttokttak.jellydiary.user.dto.UserProfileUpdateRequestDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    ResponseDto<?> getUserProflie(CustomOAuth2User customOAuth2User);

    ResponseDto<?> updateUserProfileImg(CustomOAuth2User customOAuth2User, MultipartFile newProfileImg);

    ResponseDto<?> checkUserName(CustomOAuth2User customOAuth2User, UserNameCheckRequestDto userNameCheckRequestDto);

    ResponseDto<?> updateUserProfile(CustomOAuth2User customOAuth2User, UserProfileUpdateRequestDto userProfileUpdateRequestDto);

    ResponseDto<?> updateUserNotificationSetting(CustomOAuth2User customOAuth2User, UserNotificationSettingRequestDto userNotificationSettingRequestDto);

    ResponseDto<?> deleteUser(HttpServletRequest request, HttpServletResponse response, CustomOAuth2User customOAuth2User);
}
