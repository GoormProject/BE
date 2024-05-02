package com.ttokttak.jellydiary.comment.service;

import com.ttokttak.jellydiary.comment.dto.*;
import com.ttokttak.jellydiary.comment.entity.CommentEntity;
import com.ttokttak.jellydiary.comment.entity.CommentTagCompositeKey;
import com.ttokttak.jellydiary.comment.entity.CommentTagEntity;
import com.ttokttak.jellydiary.comment.repository.CommentRepository;
import com.ttokttak.jellydiary.comment.repository.CommentTagRepository;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserRoleEnum;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diary.repository.DiaryUserRepository;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.like.dto.PostLikeMapper;
import com.ttokttak.jellydiary.like.repository.PostLikeRepository;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.CREATE_COMMENT_SUCCESS;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final DiaryProfileRepository diaryProfileRepository;
    private final DiaryPostRepository diaryPostRepository;
    private final DiaryUserRepository diaryUserRepository;
    private final CommentRepository commentRepository;
    private final CommentTagRepository commentTagRepository;
    private final CommentMapper commentMapper;
    private final CommentTagMapper commentTagMapper;
    private final PostLikeMapper postLikeMapper;

    @Transactional
    @Override
    public ResponseDto<?> createComment(Long postId, CommentCreateRequestDto commentCreateRequestDto, CustomOAuth2User customOAuth2User) {
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

        //CommentEntity 생성 후 저장
        CommentEntity commentEntity = commentMapper.commentCreateRequestToEntity(commentCreateRequestDto, userEntity, diaryPostEntity);
        commentRepository.save(commentEntity);

        Set<CommentUserTagInfoDto> commentUserTagInfoDtos = new HashSet<>();
        //CommentTag entity 생성 후 저장
        for (Long userId : commentCreateRequestDto.getUserTag()) {
            UserEntity tagUserEntity = userRepository.findById(userId).orElseThrow(
                    () -> new CustomException(USER_TAG_NOT_FOUND)
            );
            CommentTagCompositeKey commentTagCompositeKey = commentTagMapper.userAndCommentToCommentTagCompositeKey(tagUserEntity.getUserId(), commentEntity.getCommentId());
            CommentTagEntity commentTagEntity = commentTagMapper.commentCreateRequestToEntity(commentTagCompositeKey, tagUserEntity, commentEntity);
            commentTagRepository.save(commentTagEntity);

            CommentUserTagInfoDto commentUserTagInfoDto = commentTagMapper.entityToCommentUserInfoDto(tagUserEntity);
            commentUserTagInfoDtos.add(commentUserTagInfoDto);
        }

        CommentCreateCommentInfoDto commentCreateCommentInfoDto = commentMapper.entityAndDtoToCommentInfoDto(userEntity, commentEntity, commentUserTagInfoDtos);
        CommentCreateResponseDto commentCreateResponseDto = commentMapper.dtoToCommentCreateResponseDto(diaryPostEntity.getPostId(), commentCreateCommentInfoDto);

        return ResponseDto.builder()
                .statusCode(CREATE_COMMENT_SUCCESS.getHttpStatus().value())
                .message(CREATE_COMMENT_SUCCESS.getDetail())
                .data(commentCreateResponseDto)
                .build();
    }
}
