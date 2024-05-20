package com.ttokttak.jellydiary.user.service;

import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.jwt.JWTUtil;
import com.ttokttak.jellydiary.notification.entity.NotificationSettingEntity;
import com.ttokttak.jellydiary.notification.repository.NotificationSettingRepository;
import com.ttokttak.jellydiary.user.dto.*;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.RefreshTokenEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.entity.UserStateEnum;
import com.ttokttak.jellydiary.user.mapper.UserMapper;
import com.ttokttak.jellydiary.user.repository.RefreshTokenRepository;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.S3Uploader;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
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
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

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

    @Override
    @Transactional
    public ResponseDto<?> deleteUser(HttpServletRequest request, HttpServletResponse response, CustomOAuth2User customOAuth2User) {
        // Get refresh token from cookies
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
                break;
            }
        }

        // Refresh 토큰이 있는지 확인
        if (refresh == null) {
            throw new CustomException(REFRESH_TOKEN_NULL);
        }

        Long userId = jwtUtil.getUserId(refresh);
        String category = jwtUtil.getCategory(refresh);
        String refreshTokenEntityId = userId + ":" + refresh;

        // Refresh 토큰이 만료되었는지 확인(Validate)
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomException(REFRESH_TOKEN_EXPIRED);
        }

        // Refresh 토큰의 category가 refresh인지 확인 (발급시 페이로드에 명시) (Verify)
        if (!category.equals("refresh")) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }

        // 들어온 Refresh 토큰이 가장 최근에 발급된 토큰인지 확인
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findById(refreshTokenEntityId)
                .orElseThrow(() -> new CustomException(INVALID_REFRESH_TOKEN));

        // 쿠키에서 추출한 Refresh 토큰값이 RefreshTokenEntity의 refreshToken값과 같은지 확인
        if (!refreshTokenEntity.getRefreshToken().equals(refresh)) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }

        // 로그아웃 진행
        // Refresh 토큰 DB에서 제거
        refreshTokenRepository.deleteById(refreshTokenEntityId);

        // 기존 Refresh 토큰의 쿠기 만료
        ResponseCookie expiredRefreshTokenCookie = ResponseCookie.from("refresh", "")
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, expiredRefreshTokenCookie.toString());

        // 회원탈퇴 진행
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (userEntity.getUserState() != UserStateEnum.ACTIVE) {
            throw new CustomException(USER_ACCOUNT_DISABLED);
        }

        userEntity.updateUserState(UserStateEnum.INACTIVE, false);

        NotificationSettingEntity notificationSettingEntity = notificationSettingRepository.findById(userEntity.getUserId())
                .orElseThrow(() -> new CustomException(NOTIFICATION_SETTINGS_NOT_FOUND));

        notificationSettingEntity.notificationsSettingUpdate(false, false, false, false, false, false, false, false);

        return ResponseDto.builder()
                .statusCode(DELETE_USER_SUCCESS.getHttpStatus().value())
                .message(DELETE_USER_SUCCESS.getDetail())
                .build();
    }
}
