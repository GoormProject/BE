package com.ttokttak.jellydiary.diary.service;

import com.ttokttak.jellydiary.diary.dto.DiaryProfileRequestDto;
import com.ttokttak.jellydiary.diary.dto.DiaryProfileResponseDto;
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
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

@Service
@AllArgsConstructor
@Slf4j
public class DiaryProfileServiceImpl implements DiaryProfileService{

    private final DiaryProfileRepository diaryProfileRepository;

    private final UserRepository userRepository;

    private final DiaryUserRepository diaryUserRepository;

    private final DiaryProfileMapper diaryProfileMapper;

    @Override
    public ResponseDto<?> createDiaryProfile(DiaryProfileRequestDto diaryProfileRequestDto, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //TODO: [작성자 정다운]다이어리 채팅방(chat_room, chat_user) 생성 코드 추가하기

        DiaryProfileEntity diaryEntity = diaryProfileMapper.diaryProfileRequestDtoToEntity(diaryProfileRequestDto);
        DiaryProfileEntity savedEntity = diaryProfileRepository.save(diaryEntity);

        DiaryUserEntity diaryUser = DiaryUserEntity.builder()
                .diaryId(savedEntity)
                .userId(userEntity)
                .diaryRole(DiaryUserRoleEnum.CREATOR)
                .isInvited(null)
                .build();
        diaryUserRepository.save(diaryUser);

        return ResponseDto.builder()
                .statusCode(CREATE_DIARY_PROFILE_SUCCESS.getHttpStatus().value())
                .message(CREATE_DIARY_PROFILE_SUCCESS.getDetail())
                .data(diaryProfileMapper.entityToDiaryProfileResponseDto(savedEntity))
                .build();
    }

}
