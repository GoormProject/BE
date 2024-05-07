package com.ttokttak.jellydiary.user.service;

import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.notification.entity.NotificationSettingEntity;
import com.ttokttak.jellydiary.notification.repository.NotificationSettingRepository;
import com.ttokttak.jellydiary.user.dto.UserProfileDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.entity.UserStateEnum;
import com.ttokttak.jellydiary.user.mapper.UserMapper;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.GET_USER_PROFILE_SUCCESS;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final NotificationSettingRepository notificationSettingRepository;

    @Override
    public ResponseDto<?> getUserProflie(CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if(userEntity.getUserState() != UserStateEnum.ACTIVE) {
            throw new CustomException(USER_ACCOUNT_DISABLED);
        }

        NotificationSettingEntity notificationSettingEntity = notificationSettingRepository.findById(userEntity.getUserId())
                .orElseThrow(() -> new CustomException(NOTIFICATION_SETTINGS_NOT_FOUND));

        UserProfileDto userProfileDto = UserMapper.INSTANCE.entitiytoUserProfileDto(userEntity, notificationSettingEntity);

        return ResponseDto.builder()
                .statusCode(GET_USER_PROFILE_SUCCESS.getHttpStatus().value())
                .message(GET_USER_PROFILE_SUCCESS.getDetail())
                .data(userProfileDto)
                .build();
    }


}
