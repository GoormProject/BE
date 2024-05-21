package com.ttokttak.jellydiary.sns.service;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostImgEntity;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostImgRepository;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.like.entity.PostLikeEntity;
import com.ttokttak.jellydiary.like.repository.PostLikeRepository;
import com.ttokttak.jellydiary.sns.dto.SnsGetResponseDto;
import com.ttokttak.jellydiary.sns.mapper.SnsMapper;
import com.ttokttak.jellydiary.sns.dto.SnsGetListResponseDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.USER_NOT_FOUND;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.GET_SNS_LIST_SUCCESS;

@Service
@RequiredArgsConstructor
public class SnsServiceImpl implements SnsService {
    private final UserRepository userRepository;
    private final DiaryPostRepository diaryPostRepository;
    private final DiaryPostImgRepository diaryPostImgRepository;
    private final PostLikeRepository postLikeRepository;
    private final SnsMapper snsMapper;

    @Transactional
    @Override
    public ResponseDto<?> getSnsList(Pageable pageable, Long lastPostId, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Slice<DiaryPostEntity> diaryPostEntities = diaryPostRepository.postOrderByCreatedAtDesc(userEntity, pageable, lastPostId);

        List<SnsGetListResponseDto> snsGetListResponseDtos = new ArrayList<>();
        for (DiaryPostEntity diaryPostEntity : diaryPostEntities) {
            boolean isLike;
            Optional<PostLikeEntity> dbPostLikeentity = postLikeRepository.findByUserAndDiaryPost(userEntity, diaryPostEntity);
            if (dbPostLikeentity.isPresent()) {
                isLike = true;
            } else {
                isLike = false;
            }

            DiaryPostImgEntity firstDiaryPostImg = diaryPostImgRepository.findFirstByDiaryPostAndIsDeleted(diaryPostEntity, false);
            SnsGetListResponseDto snsGetListResponseDto = snsMapper.entityToSnsGetListResponseDto(diaryPostEntity.getUser(), diaryPostEntity, diaryPostEntity.getDiaryProfile(), firstDiaryPostImg, isLike);
            snsGetListResponseDtos.add(snsGetListResponseDto);
        }

        boolean hasNext = diaryPostEntities.hasNext();

        SnsGetResponseDto snsGetResponseDto = snsMapper.dtoToSnsGetResponseDto(snsGetListResponseDtos, hasNext);

        return ResponseDto.builder()
                .statusCode(GET_SNS_LIST_SUCCESS.getHttpStatus().value())
                .message(GET_SNS_LIST_SUCCESS.getDetail())
                .data(snsGetResponseDto)
                .build();
    }
}
