package com.ttokttak.jellydiary.like.dto;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.like.entity.PostLikeCompositeKey;
import com.ttokttak.jellydiary.like.entity.PostLikeEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;


import static com.ttokttak.jellydiary.exception.message.ErrorMsg.USER_NOT_FOUND;

@Mapper(componentModel = "spring")
public interface PostLikeMapper {
    PostLikeMapper INSTANCE = Mappers.getMapper(PostLikeMapper.class);

    PostLikeCompositeKey dtoToCompositeKeyEntity(Long userId, Long diaryPostId);

    @Mapping(target = "id", source = "postLikeCompositeKey")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "diaryPost", source = "diaryPost")
    PostLikeEntity compositeKeyAndUserAndPostToPostLikeEntity(PostLikeCompositeKey postLikeCompositeKey, UserEntity user, DiaryPostEntity diaryPost);

}
