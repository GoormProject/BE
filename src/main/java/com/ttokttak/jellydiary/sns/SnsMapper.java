package com.ttokttak.jellydiary.sns;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostImgEntity;
import com.ttokttak.jellydiary.sns.dto.SnsGetListResponseDto;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SnsMapper {
    SnsMapper INSTANCE = Mappers.getMapper(SnsMapper.class);
/*
*  private Long userId;
    private String userName;
    private String userProfileImg;
    private Long postId;
    private Long diaryId;
    private String diaryProfileImage;
    private boolean isLike;*/
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "userProfileImg", source = "user.profileImg")
    @Mapping(target = "postId", source = "diaryPost.postId")
    @Mapping(target = "postImg", source = "diaryPostImg.imageLink")
    @Mapping(target = "diaryId", source = "diaryProfile.diaryId")
    @Mapping(target = "diaryProfileImage", source = "diaryProfile.diaryProfileImage")
    SnsGetListResponseDto entityToSnsGetListResponseDto(UserEntity user, DiaryPostEntity diaryPost, DiaryProfileEntity diaryProfile, DiaryPostImgEntity diaryPostImg, boolean isLike);
}
