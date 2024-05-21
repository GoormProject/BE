package com.ttokttak.jellydiary.like.dto;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.like.entity.PostLikeCompositeKey;
import com.ttokttak.jellydiary.like.entity.PostLikeEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostLikeMapper {
    PostLikeMapper INSTANCE = Mappers.getMapper(PostLikeMapper.class);

    PostLikeCompositeKey dtoToCompositeKeyEntity(Long userId, Long diaryPostId);

    @Mapping(target = "id", source = "postLikeCompositeKey")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "diaryPost", source = "diaryPost")
    PostLikeEntity compositeKeyAndUserAndPostToPostLikeEntity(PostLikeCompositeKey postLikeCompositeKey, UserEntity user, DiaryPostEntity diaryPost);

}
