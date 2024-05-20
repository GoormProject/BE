package com.ttokttak.jellydiary.user.service;

import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.notification.entity.NotificationSettingEntity;
import com.ttokttak.jellydiary.notification.repository.NotificationSettingRepository;
import com.ttokttak.jellydiary.user.dto.*;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.entity.UserStateEnum;
import com.ttokttak.jellydiary.user.mapper.UserMapper;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.S3Uploader;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final S3Uploader s3Uploader;

    @Override
    public ResponseDto<?> getUserProflie(CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (userEntity.getUserState() != UserStateEnum.ACTIVE) {
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

    @Override
    public ResponseDto<?> updateUserProfileImg(CustomOAuth2User customOAuth2User, MultipartFile newProfileImg) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (userEntity.getUserState() != UserStateEnum.ACTIVE) {
            throw new CustomException(USER_ACCOUNT_DISABLED);
        }

        String s3Path = "profile/" + UUID.randomUUID();
        if (userEntity.getProfileImg() != null) { // 기존 이미지가 있는 경우
            // 기존 이미지 S3 경로 추출
            String keyToDelete = s3Uploader.extractKeyFromUrl(userEntity.getProfileImg());

            // 기존 이미지 삭제
            s3Uploader.deleteObject(keyToDelete);

            if (newProfileImg == null || newProfileImg.isEmpty()) { // 새 이미지가 null인 경우
                // DB에 null로 업데이트
                userEntity.uploadProfileImg(null);
            } else { // 새 이미지가 있는 경우
                // DB에 새 이미지 업로드
                String newImageUrl = s3Uploader.uploadToS3(newProfileImg, s3Path);
                userEntity.uploadProfileImg(newImageUrl);
            }
        } else { // 기존 이미지가 없는 경우
            if (newProfileImg == null || newProfileImg.isEmpty()) { // 새 이미지가 null인 경우
                // 새 이미지가 null이면 DB에는 변화를 주지 않아 아무 작업도 수행하지 않음
            } else { // 새 이미지가 있는 경우
                // 새 이미지 업로드
                String newImageUrl = s3Uploader.uploadToS3(newProfileImg, s3Path);
                userEntity.uploadProfileImg(newImageUrl);
            }
        }

        userRepository.save(userEntity);

        return ResponseDto.builder()
                .statusCode(UPDATE_USER_PROFILE_IMAGE_SUCCESS.getHttpStatus().value())
                .message(UPDATE_USER_PROFILE_IMAGE_SUCCESS.getDetail())
                .build();
    }

    @Override
    public ResponseDto<?> checkUserName(CustomOAuth2User customOAuth2User, UserNameCheckRequestDto userNameCheckRequestDto) {
        if (userRepository.existsByUserName(userNameCheckRequestDto.getUserName())) {
            throw new CustomException(DUPLICATE_USER_NAME);
        }

        return ResponseDto.builder()
                .statusCode(USER_NAME_CHECK_SUCCESS.getHttpStatus().value())
                .message(USER_NAME_CHECK_SUCCESS.getDetail())
                .build();
    }

    @Override
    @Transactional
    public ResponseDto<?> updateUserProfile(CustomOAuth2User customOAuth2User, UserProfileUpdateRequestDto userProfileUpdateRequestDto) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (userEntity.getUserState() != UserStateEnum.ACTIVE) {
            throw new CustomException(USER_ACCOUNT_DISABLED);
        }

        if (userRepository.existsByUserName(userProfileUpdateRequestDto.getUserName())) {
            throw new CustomException(DUPLICATE_USER_NAME);
        }

        userEntity.userProfileUpdate(userProfileUpdateRequestDto.getUserName(), userProfileUpdateRequestDto.getUserDescription());

        UserProfileUpdateResponseDto userProfileUpdateResponseDto = UserMapper.INSTANCE.entitiytoUserProfileUpdateResponseDto(userEntity);

        return ResponseDto.builder()
                .statusCode(UPDATE_USER_PROFILE_SUCCESS.getHttpStatus().value())
                .message(UPDATE_USER_PROFILE_SUCCESS.getDetail())
                .data(userProfileUpdateResponseDto)
                .build();
    }

    @Override
    @Transactional
    public ResponseDto<?> updateUserNotificationSetting(CustomOAuth2User customOAuth2User, UserNotificationSettingRequestDto userNotificationSettingRequestDto) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (userEntity.getUserState() != UserStateEnum.ACTIVE) {
            throw new CustomException(USER_ACCOUNT_DISABLED);
        }

        NotificationSettingEntity notificationSettingEntity = notificationSettingRepository.findById(userEntity.getUserId())
                .orElseThrow(() -> new CustomException(NOTIFICATION_SETTINGS_NOT_FOUND));

        userEntity.userNotificationSettingUpdate(userNotificationSettingRequestDto.getNotificationSetting());
        notificationSettingEntity.notificationsSettingUpdate(userNotificationSettingRequestDto.getSubscribe(), userNotificationSettingRequestDto.getPostLike(), userNotificationSettingRequestDto.getPostComment(), userNotificationSettingRequestDto.getPost(), userNotificationSettingRequestDto.getDiary(), userNotificationSettingRequestDto.getCommentTag(), userNotificationSettingRequestDto.getNewFollower(), userNotificationSettingRequestDto.getDm());

        UserNotificationSettingResponseDto userNotificationSettingResponseDto = UserMapper.INSTANCE.entitiytoUserNotificationSettingResponseDto(userEntity, notificationSettingEntity);

        return ResponseDto.builder()
                .statusCode(UPDATE_USER_NOTIFICATION_SETTING_SUCCESS.getHttpStatus().value())
                .message(UPDATE_USER_NOTIFICATION_SETTING_SUCCESS.getDetail())
                .data(userNotificationSettingResponseDto)
                .build();
    }
}
