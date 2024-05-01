package com.ttokttak.jellydiary.feed.service;

import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.feed.dto.TargetUserInfoResponseDto;
import com.ttokttak.jellydiary.feed.mapper.FeedMapper;
import com.ttokttak.jellydiary.follow.repository.FollowRepository;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedServiceImpl implements FeedService {

    private final FollowRepository followRepository;

    private final UserRepository userRepository;

    private final FeedMapper feedMapper;

    @Override
    @Transactional
    public ResponseDto<?> getTargetUserFeedInfo(Long targetUserId) {
        UserEntity userEntity = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        TargetUserInfoResponseDto targetUserInfoResponseDto = feedMapper.userEntityToTargetUserInfoResponseDto(userEntity);
        targetUserInfoResponseDto.setFollowerCount(followRepository.countByIdFollowResponseId(userEntity.getUserId()));
        targetUserInfoResponseDto.setFollowingCount(followRepository.countByIdFollowRequestId(userEntity.getUserId()));

        return ResponseDto.builder()
                .statusCode(SEARCH_TARGET_USER_FEED_INFO_SUCCESS.getHttpStatus().value())
                .message(SEARCH_TARGET_USER_FEED_INFO_SUCCESS.getDetail())
                .data(targetUserInfoResponseDto)
                .build();
    }
}
