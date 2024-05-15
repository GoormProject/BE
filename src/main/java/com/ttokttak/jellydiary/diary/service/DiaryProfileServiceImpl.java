package com.ttokttak.jellydiary.diary.service;

import com.ttokttak.jellydiary.chat.entity.ChatRoomEntity;
import com.ttokttak.jellydiary.chat.entity.ChatUserEntity;
import com.ttokttak.jellydiary.chat.repository.ChatRoomRepository;
import com.ttokttak.jellydiary.chat.repository.ChatUserRepository;
import com.ttokttak.jellydiary.diary.dto.DiaryProfileRequestDto;
import com.ttokttak.jellydiary.diary.dto.DiaryProfileUpdateRequestDto;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserRoleEnum;
import com.ttokttak.jellydiary.diary.mapper.DiaryProfileMapper;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diary.repository.DiaryUserRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.S3Uploader;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryProfileServiceImpl implements DiaryProfileService{

    private final DiaryProfileRepository diaryProfileRepository;

    private final UserRepository userRepository;

    private final DiaryUserRepository diaryUserRepository;

    private final DiaryProfileMapper diaryProfileMapper;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatUserRepository chatUserRepository;

    private final S3Uploader s3Uploader;

    @Transactional
    @Override
    public ResponseDto<?> createDiaryProfile(DiaryProfileRequestDto diaryProfileRequestDto, MultipartFile diaryProfileImage, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if(diaryProfileImage != null && !diaryProfileImage.isEmpty()){
            String s3Path = "diary_profile/" + UUID.randomUUID();
            String imageUrl = s3Uploader.uploadToS3(diaryProfileImage, s3Path);
            diaryProfileRequestDto.setDiaryProfileImage(imageUrl);
        }

        DiaryProfileEntity diaryEntity = diaryProfileMapper.diaryProfileRequestDtoToEntity(diaryProfileRequestDto);
        DiaryProfileEntity savedEntity = diaryProfileRepository.save(diaryEntity);

        DiaryUserEntity diaryUser = DiaryUserEntity.builder()
                .diaryId(savedEntity)
                .userId(userEntity)
                .diaryRole(DiaryUserRoleEnum.CREATOR)
                .isInvited(null)
                .build();
        diaryUserRepository.save(diaryUser);
        
        String chatRoomName = "group_" + savedEntity.getDiaryId();
        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .chatRoomName(chatRoomName)
                .build();
        ChatRoomEntity savedChatRoomEntity = chatRoomRepository.save(chatRoomEntity);

        ChatUserEntity chatUserEntity = ChatUserEntity.builder().
                chatRoomId(savedChatRoomEntity).
                userId(userEntity)
                .build();
        chatUserRepository.save(chatUserEntity);

        savedEntity.assignChatRoom(chatRoomEntity);

        return ResponseDto.builder()
                .statusCode(CREATE_DIARY_PROFILE_SUCCESS.getHttpStatus().value())
                .message(CREATE_DIARY_PROFILE_SUCCESS.getDetail())
                .data(diaryProfileMapper.entityToDiaryProfileResponseDto(savedEntity))
                .build();
    }

    @Transactional
    @Override
    public ResponseDto<?> updateDiaryProfile(Long diaryId, DiaryProfileUpdateRequestDto diaryProfileUpdateRequestDto, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

        DiaryUserEntity byDiaryIdAndUserId = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity)
                .orElseThrow(() -> new CustomException(YOU_ARE_NOT_A_DIARY_CREATOR));

        if(!byDiaryIdAndUserId.getDiaryRole().equals(DiaryUserRoleEnum.CREATOR)){
            throw new CustomException(YOU_ARE_NOT_A_DIARY_CREATOR);
        }

        diaryProfileEntity.diaryProfileUpdate(diaryProfileUpdateRequestDto.getDiaryName(), diaryProfileUpdateRequestDto.getDiaryDescription());

        return ResponseDto.builder()
                .statusCode(UPDATE_DIARY_PROFILE_SUCCESS.getHttpStatus().value())
                .message(UPDATE_DIARY_PROFILE_SUCCESS.getDetail())
                .data(diaryProfileMapper.entityToDiaryProfileUpdateResponseDto(diaryProfileEntity))
                .build();
    }

    @Override
    public ResponseDto<?> getDiaryProfileInfo(Long diaryId) {
        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

        return ResponseDto.builder()
                .statusCode(SEARCH_DIARY_SUCCESS.getHttpStatus().value())
                .message(SEARCH_DIARY_SUCCESS.getDetail())
                .data(diaryProfileMapper.entityToDiaryProfileResponseDto(diaryProfileEntity))
                .build();
    }

    @Override
    public ResponseDto<?> getMySubscribedOrParticipatingDiariesList(CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<DiaryUserEntity> diaryUsers = diaryUserRepository.findByUserId(userEntity);
        List<DiaryProfileEntity> diaryProfileEntityList = diaryUsers.stream()
                .map(DiaryUserEntity::getDiaryId)
                .filter(diaryProfileEntity -> !diaryProfileEntity.getIsDiaryDeleted())
                .toList();

        return ResponseDto.builder()
                .statusCode(SEARCH_MY_DIARY_LIST_SUCCESS.getHttpStatus().value())
                .message(SEARCH_MY_DIARY_LIST_SUCCESS.getDetail())
                .data(diaryProfileMapper.entityToDiaryProfileResponseDtoList(diaryProfileEntityList))
                .build();
    }

    @Transactional
    @Override
    public ResponseDto<?> deleteDiaryProfile(Long diaryId, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

        DiaryUserEntity byDiaryIdAndUserId = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity)
                .orElseThrow(() -> new CustomException(YOU_ARE_NOT_A_DIARY_CREATOR));

        if(!byDiaryIdAndUserId.getDiaryRole().equals(DiaryUserRoleEnum.CREATOR)){
            throw new CustomException(YOU_ARE_NOT_A_DIARY_CREATOR);
        }

        diaryProfileRepository.delete(diaryProfileEntity);

        return ResponseDto.builder()
                .statusCode(DELETE_DIARY_PROFILE_SUCCESS.getHttpStatus().value())
                .message(DELETE_DIARY_PROFILE_SUCCESS.getDetail())
                .build();
    }

    @Transactional
    @Override
    public ResponseDto<?> updateDiaryProfileImg(Long diaryId, MultipartFile diaryProfileImage, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        DiaryUserEntity byDiaryIdAndUserId = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity)
                .orElseThrow(() -> new CustomException(YOU_ARE_NOT_A_DIARY_CREATOR));

        if(!byDiaryIdAndUserId.getDiaryRole().equals(DiaryUserRoleEnum.CREATOR)){
            throw new CustomException(YOU_ARE_NOT_A_DIARY_CREATOR);
        }

        if(diaryProfileEntity.getDiaryProfileImage() != null){
            String keyToDelete = s3Uploader.extractKeyFromUrl(diaryProfileEntity.getDiaryProfileImage());
            s3Uploader.deleteObject(keyToDelete);
        }

        if(diaryProfileImage != null && !diaryProfileImage.isEmpty()){
            String s3Path = "diary_profile/" + UUID.randomUUID();
            String imageUrl = s3Uploader.uploadToS3(diaryProfileImage, s3Path);
            diaryProfileEntity.diaryProfileImgUpdate(imageUrl);
        }

        return ResponseDto.builder()
                .statusCode(UPDATE_DIARY_PROFILE_IMAGE_SUCCESS.getHttpStatus().value())
                .message(UPDATE_DIARY_PROFILE_IMAGE_SUCCESS.getDetail())
                .data(diaryProfileMapper.entityToDiaryProfileResponseDto(diaryProfileEntity))
                .build();
    }

}
