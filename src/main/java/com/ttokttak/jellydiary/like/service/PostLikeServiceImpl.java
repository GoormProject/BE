package com.ttokttak.jellydiary.like.service;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.like.dto.PostLikeMapper;
import com.ttokttak.jellydiary.like.entity.PostLikeCompositeKey;
import com.ttokttak.jellydiary.like.entity.PostLikeEntity;
import com.ttokttak.jellydiary.like.repository.PostLikeRepository;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.Like_POST_SUCCESS;

@Service
@RequiredArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {
    private final UserRepository userRepository;
    private final DiaryProfileRepository diaryProfileRepository;
    private final DiaryPostRepository diaryPostRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostLikeMapper postLikeMapper;

    @Transactional
    @Override
    public ResponseDto<?> createPostLike(Long postId, CustomOAuth2User customOAuth2User) {
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

        //복합키 생성
        PostLikeCompositeKey postLikeCompositeKey = postLikeMapper.dtoToCompositeKeyEntity(userEntity.getUserId(), diaryPostEntity.getPostId());
        //복합키와 user, diaryPost로 PostLikeEntity 생성
        PostLikeEntity postLikeEntity = postLikeMapper.compositeKeyAndUserAndPostToPostLikeEntity(postLikeCompositeKey, userEntity, diaryPostEntity);

        postLikeRepository.save(postLikeEntity);

        return ResponseDto.builder()
                .statusCode(Like_POST_SUCCESS.getHttpStatus().value())
                .message(Like_POST_SUCCESS.getDetail())
                .build();
    }
}
