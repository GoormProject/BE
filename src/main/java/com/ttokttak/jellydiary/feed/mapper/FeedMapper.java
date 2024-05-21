package com.ttokttak.jellydiary.feed.mapper;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.feed.dto.TargetUserFeedResponseDto;
import com.ttokttak.jellydiary.feed.dto.TargetUserFollowersDto;
import com.ttokttak.jellydiary.feed.dto.TargetUserInfoResponseDto;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeedMapper {
    FeedMapper INSTANCE = Mappers.getMapper(FeedMapper.class);

    @Mapping(target = "followingCount", ignore = true)
    @Mapping(target = "followerCount", ignore = true)
    @Mapping(target = "followStatus", ignore = true)
    TargetUserInfoResponseDto userEntityToTargetUserInfoResponseDto(UserEntity userEntity);

    @Mapping(target = "followStatus", ignore = true)
    TargetUserFollowersDto entityToTargetUserFollowersDto(UserEntity entity);

    @Mapping(target = "postImgIsMultiple", ignore = true)
    @Mapping(target = "postImg", ignore = true)
    TargetUserFeedResponseDto entityToTargetUserFeedResponseDto(DiaryPostEntity entity);

}
