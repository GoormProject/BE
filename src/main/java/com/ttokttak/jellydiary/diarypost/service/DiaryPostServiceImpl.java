package com.ttokttak.jellydiary.diarypost.service;

import com.ttokttak.jellydiary.comment.repository.CommentRepository;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserRoleEnum;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diary.repository.DiaryUserRepository;
import com.ttokttak.jellydiary.diarypost.dto.*;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostImgEntity;
import com.ttokttak.jellydiary.diarypost.mapper.DiaryPostImgMapper;
import com.ttokttak.jellydiary.diarypost.mapper.DiaryPostMapper;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostImgRepository;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.like.entity.PostLikeEntity;
import com.ttokttak.jellydiary.like.repository.PostLikeRepository;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final DiaryPostMapper diaryPostMapper;
    private final DiaryPostImgMapper diaryPostImgMapper;
    private final S3Uploader s3Uploader;


    //게시글 생성
    @Transactional
    @Override
    public ResponseDto<?> createDiaryPost(Long diaryId, DiaryPostCreateRequestDto diaryPostCreateRequestDto, List<MultipartFile> postImgs, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

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
        if(diaryPostEntity.getIsDeleted())
            throw new CustomException(POST_ALREADY_DELETED);

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryPostEntity.getDiaryProfile().getDiaryId())
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

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
        List<DiaryPostImgEntity> dbDiaryPostImg = diaryPostImgRepository.findAllByDiaryPostAndIsDeleted(diaryPostEntity, false);
        // 기존 이미지 중 삭제할 이미지 삭제
        if (deleteImageIds != null && !deleteImageIds.isEmpty()) {
            for (Long deleteImageId : deleteImageIds) {
                // 삭제할 이미지 검색
                DiaryPostImgEntity deleteImage = dbDiaryPostImg.stream()
                        .filter(img -> img.getPostImgId().equals(deleteImageId))
                        .findFirst()
                        .orElseThrow(() -> new CustomException(IMG_NOT_FOUND));

                // 이미지 삭제
                String keyToDelete = s3Uploader.extractKeyFromUrl(deleteImage.getImageLink());
                s3Uploader.deleteObject(keyToDelete);
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

        if (newPostImgs.size() + diaryPostImgListResponseDtos.size() > 5) {
            throw new CustomException(YOU_CAN_ONLY_UPLOAD_UP_TO_5_IMAGES);
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
        if(diaryPostEntity.getIsDeleted())
            throw new CustomException(POST_ALREADY_DELETED);

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryPostEntity.getDiaryProfile().getDiaryId())
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

        DiaryUserEntity diaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity)
                .orElseThrow(() -> new CustomException(DIARY_USER_NOT_FOUND));

        //다이어리 권한이 생성자가 아닌 경우 게시물을 삭제하지 못하도록 에러 처리
        if ((diaryUserEntity.getDiaryRole() != DiaryUserRoleEnum.CREATOR)) {
            throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_DELETE);
        }

        List<DiaryPostImgEntity> dbDiaryPostImgs = diaryPostImgRepository.findAllByDiaryPostAndIsDeleted(diaryPostEntity, false);
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

    @Override
    public ResponseDto<?> getDiaryPostList(Long diaryId, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryId)
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

        DiaryUserEntity diaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity)
                .orElseThrow(() -> new CustomException(DIARY_USER_NOT_FOUND));

        List<DiaryPostEntity> getDiaryPostList = new ArrayList<>();

        if (diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.SUBSCRIBE) {
            getDiaryPostList.addAll(diaryPostRepository.findAllByDiaryProfileAndIsPublicAAndIsDeleted(diaryProfileEntity, true, false));
        } else {
            getDiaryPostList.addAll(diaryPostRepository.findAllByDiaryProfileAndIsDeleted(diaryProfileEntity, false));
        }

        List<DiaryPostListResponseDto> diaryPostListResponseDtos = new ArrayList<>();
        for (DiaryPostEntity diaryPostEntity : getDiaryPostList) {
            diaryPostListResponseDtos.add(diaryPostMapper.entityToDiaryPostListResponseDto(diaryPostEntity, diaryProfileEntity, userEntity));
        }

        diaryPostListResponseDtos = diaryPostListResponseDtos.stream().sorted(Comparator.comparing(DiaryPostListResponseDto::getCreatedAt).reversed()).toList();
        return ResponseDto.builder()
                .statusCode(GET_LIST_POST_SUCCESS.getHttpStatus().value())
                .message(GET_LIST_POST_SUCCESS.getDetail())
                .data(diaryPostListResponseDtos)
                .build();
    }

    @Override
    public ResponseDto<?> getDiaryPostOne(Long postId, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryPostEntity diaryPostEntity = diaryPostRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        if(diaryPostEntity.getIsDeleted())
            throw new CustomException(POST_ALREADY_DELETED);

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryPostEntity.getDiaryProfile().getDiaryId())
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

        Optional<DiaryUserEntity> dbDiaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity);
        DiaryUserEntity diaryUserEntity;
        if (!diaryPostEntity.getIsPublic()) { //게시글이 비공개 게시글인 경우 구독자와 일반 사용자는 해당 게시글에 접근하지 못한다.

            if (dbDiaryUserEntity.isPresent()) { //dbDiaryUserEntity의 값이 존재한다면 구독자인지 여부 확인 후 예외처리
                diaryUserEntity = dbDiaryUserEntity.get();

                if (diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.SUBSCRIBE) {
                    throw new CustomException(SUBSCRIBE_DOES_NOT_HAVE_PERMISSION_TO_READ_PRIVATE);
                }
            } else { //dbDiaryUserEntity의 값이 존재하지 않는다면 해당 다이어리와 게시물에 연관이 없는 일반 사용자이므로 이에 따른 예외처리
                throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_READ_PRIVATE);
            }
        }

        //해당 게시물의 좋아요 수를 좋아요 테이블에서 조회
        Long countPostLike = postLikeRepository.countByDiaryPost(diaryPostEntity);

        //해당 게시물에 달린 댓글 수를 댓글 테이블에서 조회
        Long commentCount = commentRepository.countByDiaryPost(diaryPostEntity);

        List<DiaryPostImgEntity> diaryPostImgEntityList = diaryPostImgRepository.findAllByDiaryPostAndIsDeleted(diaryPostEntity, false);
        List<DiaryPostImgListResponseDto> diaryPostImgListResponseDtos = diaryPostImgEntityList.stream().map(diaryPostImgMapper::entityToDiaryPostImgListResponseDto).toList();

        DiaryPostGetOneResponseDto diaryPostGetOneResponseDto = diaryPostMapper.entityToDiaryPostGetOneResponseDto(diaryPostEntity, diaryPostImgListResponseDtos, diaryProfileEntity, userEntity, countPostLike, commentCount);

        return ResponseDto.builder()
                .statusCode(GET_ONE_POST_SUCCESS.getHttpStatus().value())
                .message(GET_ONE_POST_SUCCESS.getDetail())
                .data(diaryPostGetOneResponseDto)
                .build();
    }

    //새 이미지 추가 후 responseDto를 반환해주는 메서드
    private List<DiaryPostImgListResponseDto> getDiaryPostImgListResponseDtos(List<MultipartFile> postImgs, DiaryPostEntity diaryPostEntity) {
        List<DiaryPostImgListResponseDto> diaryPostImgListResponseDtos = new ArrayList<>();
        if (postImgs != null && !postImgs.isEmpty()) {
            for (MultipartFile postImg : postImgs) {
                String s3Path = "profile/" + UUID.randomUUID();
                String newImageUrl = s3Uploader.uploadToS3(postImg, s3Path);
                DiaryPostImgEntity diaryPostImgEntity = diaryPostImgMapper.diaryPostImgRequestToEntity(newImageUrl, diaryPostEntity);
                diaryPostImgRepository.save(diaryPostImgEntity);

                DiaryPostImgListResponseDto diaryPostImgListResponseDto = diaryPostImgMapper.entityToDiaryPostImgListResponseDto(diaryPostImgEntity);
                diaryPostImgListResponseDtos.add(diaryPostImgListResponseDto);
            }
        }
        return diaryPostImgListResponseDtos;
    }

}
