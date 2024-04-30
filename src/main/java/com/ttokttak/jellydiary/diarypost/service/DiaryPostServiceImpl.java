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
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

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


    //게시글 생성
    @Transactional
    @Override
    public ResponseDto<?> createDiaryPost(Long diaryId, DiaryPostCreateRequestDto diaryPostCreateRequestDto, List<MultipartFile> postImgs, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        DiaryUserEntity diaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity)
                .orElseThrow(() -> new CustomException(DIARY_USER_NOT_FOUND));

        //다이어리 권한이 읽기 권한 or 구독자인 경우, 초대 승인 대기 중인 사용자는 게시물을 수정하지 못하도록 에러 처리
        if ((diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.SUBSCRIBE || diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.READ) && (diaryUserEntity.getIsInvited() == null || !diaryUserEntity.getIsInvited())) {
            throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_WRITE_AND_UPDATE);
        }

        //입력받은 날짜를 LocalDate로 변환하여 오늘 날짜와 비교.
        //오늘 날짜보다 앞 선 날짜라면 예외처리
        if(LocalDate.now().compareTo(LocalDate.parse(diaryPostCreateRequestDto.getPostDate(), DateTimeFormatter.ISO_DATE)) < 0) {
            throw new CustomException(POST_DATE_IS_FROM_THE_PAST_TO_TODAY);
        }

        //게시물 저장
        DiaryPostEntity diaryPostEntity = diaryPostMapper.diaryPostCreateRequestDtoToEntity(diaryPostCreateRequestDto, userEntity, diaryProfileEntity);
        diaryPostRepository.save(diaryPostEntity);


        //게시물 이미지 저장
        List<DiaryPostImgListResponseDto> diaryPostImgListResponseDtos = getDiaryPostImgListResponseDtos(postImgs, diaryPostEntity);

        DiaryPostCreateResponseDto diaryPostCreateResponseDto = diaryPostMapper.entityToDiaryPostCreateResponseDto(diaryPostEntity, diaryPostImgListResponseDtos, diaryProfileEntity, userEntity);

        return ResponseDto.builder()
                .statusCode(CREATE_POST_SUCCESS.getHttpStatus().value())
                .message(CREATE_POST_SUCCESS.getDetail())
                .data(diaryPostCreateResponseDto)
                .build();
    }

    //게시글 수정
    @Transactional
    @Override
    public ResponseDto<?> updateDiaryPost(Long postId, DiaryPostCreateRequestDto diaryPostCreateRequestDto, List<Long> deleteImageIds, List<MultipartFile> newPostImgs, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryPostEntity diaryPostEntity = diaryPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryPostEntity.getDiaryProfile().getDiaryId())
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        DiaryUserEntity diaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity)
                .orElseThrow(() -> new CustomException(DIARY_USER_NOT_FOUND));

        //다이어리 권한이 읽기 권한 or 구독자인 경우, 초대 승인 대기 중인 사용자는 게시물을 수정하지 못하도록 에러 처리
        if ((diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.SUBSCRIBE || diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.READ) && (diaryUserEntity.getIsInvited() == null || !diaryUserEntity.getIsInvited())) {
            throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_WRITE_AND_UPDATE);
        }

        if(LocalDate.now().compareTo(LocalDate.parse(diaryPostCreateRequestDto.getPostDate(), DateTimeFormatter.ISO_DATE)) < 0) {
            throw new CustomException(POST_DATE_IS_FROM_THE_PAST_TO_TODAY);
        }

        //diaryPostEntity에 변경사항 업데이트
        diaryPostEntity.diaryPostUpdate(diaryPostCreateRequestDto);

        //TODO: [작성자: 김주희] S3 업로드 추가
        List<DiaryPostImgEntity> dbDiaryPostImg = diaryPostImgRepository.findAllByDiaryPost(diaryPostEntity);
        // 기존 이미지 중 삭제할 이미지 삭제
        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            for (Long deleteImageId : deleteImageIds) {
                // 삭제할 이미지 검색
                DiaryPostImgEntity deleteImage = dbDiaryPostImg.stream()
                        .filter(img -> img.getPostImgId().equals(deleteImageId))
                        .findFirst()
                        .orElseThrow(() -> new CustomException(IMG_NOT_FOUND));

                // 이미지 삭제
                diaryPostImgRepository.deleteById(deleteImage.getPostImgId());
            }
            // 삭제한 이미지 목록에서 제거
            dbDiaryPostImg.removeIf(img -> deleteImageIds.contains(img.getPostImgId()));
        }

        //response를 위하여 기존 이미지 dto에 추가
        List<DiaryPostImgListResponseDto> diaryPostImgListResponseDtos = new ArrayList<>();
        for (DiaryPostImgEntity diaryPostImg : dbDiaryPostImg) {
            DiaryPostImgListResponseDto diaryPostImgListResponseDto = diaryPostImgMapper.entityToDiaryPostImgListResponseDto(diaryPostImg);
            diaryPostImgListResponseDtos.add(diaryPostImgListResponseDto);
        }

        //새 이미지 추가 및 responseDto로 변환
        List<DiaryPostImgListResponseDto> newDiaryPostImgListResponseDtos = getDiaryPostImgListResponseDtos(newPostImgs, diaryPostEntity);

        diaryPostImgListResponseDtos.addAll(newDiaryPostImgListResponseDtos);
        DiaryPostCreateResponseDto diaryPostCreateResponseDto = diaryPostMapper.entityToDiaryPostCreateResponseDto(diaryPostEntity, diaryPostImgListResponseDtos, diaryProfileEntity, userEntity);

        return ResponseDto.builder()
                .statusCode(UPDATE_POST_SUCCESS.getHttpStatus().value())
                .message(UPDATE_POST_SUCCESS.getDetail())
                .data(diaryPostCreateResponseDto)
                .build();
    }

    @Transactional
    @Override
    public ResponseDto<?> deleteDiaryPost(Long postId, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryPostEntity diaryPostEntity = diaryPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryPostEntity.getDiaryProfile().getDiaryId())
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

        DiaryUserEntity diaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity)
                .orElseThrow(() -> new CustomException(DIARY_USER_NOT_FOUND));

        //다이어리 권한이 생성자가 아닌 경우 게시물을 삭제하지 못하도록 에러 처리
        if ((diaryUserEntity.getDiaryRole() != DiaryUserRoleEnum.CREATOR)) {
            throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_DELETE);
        }

        List<DiaryPostImgEntity> dbDiaryPostImgs = diaryPostImgRepository.findAllByDiaryPost(diaryPostEntity);
        List<Long> dbDiaryPostImgsIdList = new ArrayList<>();
        for (DiaryPostImgEntity dbDiaryPostImg : dbDiaryPostImgs) {
            dbDiaryPostImgsIdList.add(dbDiaryPostImg.getPostImgId());
        }
        //게시물 이미지 softDelete
        diaryPostImgRepository.deleteAllById(dbDiaryPostImgsIdList);
        //게시물 softDelete
        diaryPostRepository.deleteById(diaryPostEntity.getPostId());

        return ResponseDto.builder()
                .statusCode(DELETE_POST_SUCCESS.getHttpStatus().value())
                .message(DELETE_POST_SUCCESS.getDetail())
                .build();
    }

    //새 이미지 추가 후 responseDto를 반환해주는 메서드
    private List<DiaryPostImgListResponseDto> getDiaryPostImgListResponseDtos(List<MultipartFile> postImgs, DiaryPostEntity diaryPostEntity) {
        List<DiaryPostImgListResponseDto> diaryPostImgListResponseDtos = new ArrayList<>();
        if (postImgs != null && !postImgs.isEmpty()) {
            for (MultipartFile postImg : postImgs) {
                DiaryPostImgEntity diaryPostImgEntity = diaryPostImgMapper.diaryPostImgRequestToEntity(postImg, diaryPostEntity);
                diaryPostImgRepository.save(diaryPostImgEntity);

                DiaryPostImgListResponseDto diaryPostImgListResponseDto = diaryPostImgMapper.entityToDiaryPostImgListResponseDto(diaryPostImgEntity);
                diaryPostImgListResponseDtos.add(diaryPostImgListResponseDto);
            }
        }
        return diaryPostImgListResponseDtos;
    }

}
