package com.ttokttak.jellydiary.diarypost.service;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserRoleEnum;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diary.repository.DiaryUserRepository;
import com.ttokttak.jellydiary.diarypost.dto.DiaryPostCreateRequestDto;
import com.ttokttak.jellydiary.diarypost.dto.DiaryPostCreateResponseDto;
import com.ttokttak.jellydiary.diarypost.dto.DiaryPostImgListResponseDto;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostImgEntity;
import com.ttokttak.jellydiary.diarypost.mapper.DiaryPostImgMapper;
import com.ttokttak.jellydiary.diarypost.mapper.DiaryPostMapper;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostImgRepository;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.CREATE_POST_SUCCESS;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryPostServiceImpl implements DiaryPostService {
    private final UserRepository userRepository;
    private final DiaryProfileRepository diaryProfileRepository;
    private final DiaryUserRepository diaryUserRepository;
    private final DiaryPostRepository diaryPostRepository;
    private final DiaryPostImgRepository diaryPostImgRepository;
    private final DiaryPostMapper diaryPostMapper;
    private final DiaryPostImgMapper diaryPostImgMapper;


    @Transactional
    @Override
    public ResponseDto<?> createDiaryPost(Long diaryId, DiaryPostCreateRequestDto diaryPostCreateRequestDto, List<MultipartFile> postImgs, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        DiaryUserEntity diaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity)
                .orElseThrow(() -> new CustomException(DIARY_USER_NOT_FOUND));

        //다이어리 권한이 읽기 권한 or 구독자인 경우, 초대 승인 대기 중인 사용자는 게시물을 생성하지 못하도록 에러 처리
        if ((diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.SUBSCRIBE || diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.READ) && !diaryUserEntity.getIsInvited()) {
            throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_WRITE_AND_UPDATE);
        }

        System.out.println(LocalDate.now().compareTo(LocalDate.parse(diaryPostCreateRequestDto.getPostDate(), DateTimeFormatter.ISO_DATE)));
        System.out.println(LocalDate.now());

        //입력받은 날짜를 LocalDate로 변환하여 오늘 날짜와 비교.
        //오늘 날짜보다 앞 선 날짜라면 예외처리
        if(LocalDate.now().compareTo(LocalDate.parse(diaryPostCreateRequestDto.getPostDate(), DateTimeFormatter.ISO_DATE)) < 0) {
            throw new CustomException(POST_DATE_IS_FROM_THE_PAST_TO_TODAY);
        }
        //게시물 저장
        DiaryPostEntity diaryPostEntity = diaryPostMapper.diaryPostCreateRequestDtoToEntity(diaryPostCreateRequestDto, userEntity, diaryProfileEntity);
        diaryPostRepository.save(diaryPostEntity);


        //게시물 이미지 저장
        List<DiaryPostImgListResponseDto> diaryPostImgListResponseDtos = new ArrayList<>();
        if (postImgs != null && !postImgs.isEmpty()) {
            for (MultipartFile postImg : postImgs) {
                DiaryPostImgEntity diaryPostImgEntity = diaryPostImgMapper.diaryPostImgRequestToEntity(postImg, diaryPostEntity);
                diaryPostImgRepository.save(diaryPostImgEntity);

                DiaryPostImgListResponseDto diaryPostImgListResponseDto = diaryPostImgMapper.entityToDiaryPostImgListResponseDto(diaryPostImgEntity);
                diaryPostImgListResponseDtos.add(diaryPostImgListResponseDto);
            }
        }


        DiaryPostCreateResponseDto diaryPostCreateResponseDto = diaryPostMapper.entityToDiaryPostCreateResponseDto(diaryPostEntity, diaryPostImgListResponseDtos, diaryProfileEntity, userEntity);

        return ResponseDto.builder()
                .statusCode(CREATE_POST_SUCCESS.getHttpStatus().value())
                .message(CREATE_POST_SUCCESS.getDetail())
                .data(diaryPostCreateResponseDto)
                .build();
    }



}
