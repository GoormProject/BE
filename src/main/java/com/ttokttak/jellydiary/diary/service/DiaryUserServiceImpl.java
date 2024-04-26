package com.ttokttak.jellydiary.diary.service;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserRoleEnum;
import com.ttokttak.jellydiary.diary.mapper.DiaryUserMapper;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diary.repository.DiaryUserRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryUserServiceImpl implements DiaryUserService{

    private final DiaryProfileRepository diaryProfileRepository;

    private final DiaryUserRepository diaryUserRepository;

    private final DiaryUserMapper diaryUserMapper;

    @Override
    public ResponseDto<?> getDiaryParticipantsList(Long diaryId) {
        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        List<DiaryUserEntity> diaryUserEntities = diaryUserRepository.findByDiaryIdAndDiaryRoleNot(diaryProfileEntity, DiaryUserRoleEnum.SUBSCRIBE);

        return ResponseDto.builder()
                .statusCode(SEARCH_DIARY_USER_LIST_SUCCESS.getHttpStatus().value())
                .message(SEARCH_DIARY_USER_LIST_SUCCESS.getDetail())
                .data(diaryUserMapper.entityToDiaryUserResponseDto(diaryUserEntities))
                .build();
    }
}
