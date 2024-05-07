package com.ttokttak.jellydiary.feed.service;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostImgEntity;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostImgRepository;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.feed.dto.TargetUserFeedListResponseDto;
import com.ttokttak.jellydiary.feed.dto.TargetUserFeedResponseDto;
import com.ttokttak.jellydiary.feed.dto.TargetUserFollowersDto;
import com.ttokttak.jellydiary.feed.dto.TargetUserInfoResponseDto;
import com.ttokttak.jellydiary.feed.mapper.FeedMapper;
import com.ttokttak.jellydiary.follow.entity.FollowCompositeKey;
import com.ttokttak.jellydiary.follow.entity.FollowEntity;
import com.ttokttak.jellydiary.follow.repository.FollowRepository;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedServiceImpl implements FeedService {

    private final FollowRepository followRepository;

    private final UserRepository userRepository;

    private final FeedMapper feedMapper;

    private final DiaryPostRepository diaryPostRepository;

    private final DiaryPostImgRepository diaryPostImgRepository;

    @Override
    @Transactional
    public ResponseDto<?> getTargetUserFeedInfo(Long targetUserId, CustomOAuth2User customOAuth2User) {
        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        UserEntity targetUserEntity = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        TargetUserInfoResponseDto targetUserInfoResponseDto = feedMapper.userEntityToTargetUserInfoResponseDto(targetUserEntity);
        targetUserInfoResponseDto.setFollowerCount(followRepository.countByIdFollowResponseId(targetUserEntity.getUserId()));
        targetUserInfoResponseDto.setFollowingCount(followRepository.countByIdFollowRequestId(targetUserEntity.getUserId()));

        if(loginUserEntity.getUserId().equals(targetUserId)){
            targetUserInfoResponseDto.setFollowStatus(null);
        }else{
            targetUserInfoResponseDto.setFollowStatus(followRepository.existsById(new FollowCompositeKey(loginUserEntity.getUserId(), targetUserId)));
        }

        return ResponseDto.builder()
                .statusCode(SEARCH_TARGET_USER_FEED_INFO_SUCCESS.getHttpStatus().value())
                .message(SEARCH_TARGET_USER_FEED_INFO_SUCCESS.getDetail())
                .data(targetUserInfoResponseDto)
                .build();
    }

    @Override
    @Transactional
    public ResponseDto<?> createFollowRequest(Long targetUserId, CustomOAuth2User customOAuth2User) {
        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        UserEntity targetUserEntity = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if(loginUserEntity.getUserId().equals(targetUserId)){
            throw new CustomException(CANNOT_FOLLOW_SELF);
        }

        FollowEntity followEntity = FollowEntity.builder()
                .followRequestId(loginUserEntity.getUserId())
                .followResponseId(targetUserId)
                .followRequest(loginUserEntity)
                .followResponse(targetUserEntity)
                .build();

        followRepository.save(followEntity);

        return ResponseDto.builder()
                .statusCode(FOLLOW_REQUEST_SUCCESS.getHttpStatus().value())
                .message(FOLLOW_REQUEST_SUCCESS.getDetail())
                .build();
    }

    @Override
    @Transactional
    public ResponseDto<?> getTargetUserFollowerList(Long targetUserId, CustomOAuth2User customOAuth2User) {
        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<FollowEntity> followEntityList = followRepository.findByIdFollowResponseId(targetUserId);
        List<UserEntity> followerUserInfoList = followEntityList.stream().map(FollowEntity::getFollowRequest).toList();

        List<TargetUserFollowersDto> targetUserFollowersDtoList = new ArrayList<>();
        for(UserEntity userEntity : followerUserInfoList){
            TargetUserFollowersDto targetUserFollowersDto = feedMapper.entityToTargetUserFollowersDto(userEntity);

            if(loginUserEntity.getUserId().equals(userEntity.getUserId())){
                targetUserFollowersDto.setFollowStatus(null);
            }else{
                targetUserFollowersDto.setFollowStatus(followRepository.existsById(new FollowCompositeKey(loginUserEntity.getUserId(), userEntity.getUserId())));
            }

            targetUserFollowersDtoList.add(targetUserFollowersDto);
        }

        return ResponseDto.builder()
                .statusCode(SEARCH_TARGET_USER_FOLLOWER_LIST_SUCCESS.getHttpStatus().value())
                .message(SEARCH_TARGET_USER_FOLLOWER_LIST_SUCCESS.getDetail())
                .data(targetUserFollowersDtoList)
                .build();
    }

    @Override
    @Transactional
    public ResponseDto<?> getTargetUserFollowList(Long targetUserId, CustomOAuth2User customOAuth2User) {
        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<FollowEntity> followEntityList = followRepository.findByIdFollowRequestId(targetUserId);
        List<UserEntity> followerUserInfoList = followEntityList.stream().map(FollowEntity::getFollowResponse).toList();

        List<TargetUserFollowersDto> targetUserFollowersDtoList = new ArrayList<>();
        for(UserEntity userEntity : followerUserInfoList){
            TargetUserFollowersDto targetUserFollowersDto = feedMapper.entityToTargetUserFollowersDto(userEntity);

            if(loginUserEntity.getUserId().equals(userEntity.getUserId())){
                targetUserFollowersDto.setFollowStatus(null);
            }else{
                targetUserFollowersDto.setFollowStatus(followRepository.existsById(new FollowCompositeKey(loginUserEntity.getUserId(), userEntity.getUserId())));
            }

            targetUserFollowersDtoList.add(targetUserFollowersDto);
        }

        return ResponseDto.builder()
                .statusCode(SEARCH_TARGET_USER_FOLLOW_LIST_SUCCESS.getHttpStatus().value())
                .message(SEARCH_TARGET_USER_FOLLOW_LIST_SUCCESS.getDetail())
                .data(targetUserFollowersDtoList)
                .build();
    }

    @Override
    @Transactional
    public ResponseDto<?> cancelFollow(Long targetUserId, CustomOAuth2User customOAuth2User) {
        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        UserEntity targetUserEntity = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        FollowEntity unFollowEntity = FollowEntity.builder()
                .followRequestId(loginUserEntity.getUserId())
                .followResponseId(targetUserId)
                .followRequest(loginUserEntity)
                .followResponse(targetUserEntity)
                .build();

        followRepository.delete(unFollowEntity);

        return ResponseDto.builder()
                .statusCode(UNFOLLOW_SUCCESS.getHttpStatus().value())
                .message(UNFOLLOW_SUCCESS.getDetail())
                .build();
    }

    @Override
    @Transactional
    public ResponseDto<?> getTargetUserFeedList(Long targetUserId, CustomOAuth2User customOAuth2User) {
        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        UserEntity targetUserEntity = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<DiaryPostEntity> diaryPostEntityList = diaryPostRepository.findFeedListForTargetUser(loginUserEntity, targetUserEntity ,false);
        List<TargetUserFeedResponseDto> targetUserFeedResponseDtoList = new ArrayList<>();

        for(DiaryPostEntity diaryPostEntity : diaryPostEntityList){
            TargetUserFeedResponseDto targetUserFeedResponseDto = feedMapper.entityToTargetUserFeedResponseDto(diaryPostEntity);

            List<DiaryPostImgEntity> diaryPostImgEntityList = diaryPostImgRepository.findByDiaryPostAndIsDeletedOrderByPostImgIdAsc(diaryPostEntity, false);

            targetUserFeedResponseDto.setPostImgIsMultiple(diaryPostImgEntityList.size() >= 2);

            if(!diaryPostImgEntityList.isEmpty())
                targetUserFeedResponseDto.setPostImg(diaryPostImgEntityList.get(0).getImageLink());

            targetUserFeedResponseDtoList.add(targetUserFeedResponseDto);
        }

        TargetUserFeedListResponseDto targetUserFeedListResponseDto = TargetUserFeedListResponseDto.builder()
                .count(targetUserFeedResponseDtoList.size())
                .feeds(targetUserFeedResponseDtoList)
                .build();

        return ResponseDto.builder()
                .statusCode(SEARCH_TARGET_USER_FEED_LIST_SUCCESS.getHttpStatus().value())
                .message(SEARCH_TARGET_USER_FEED_LIST_SUCCESS.getDetail())
                .data(targetUserFeedListResponseDto)
                .build();
    }

}
