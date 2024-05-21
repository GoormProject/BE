package com.ttokttak.jellydiary.comment.dto;

import com.ttokttak.jellydiary.comment.entity.CommentEntity;
import com.ttokttak.jellydiary.comment.entity.CommentTagCompositeKey;
import com.ttokttak.jellydiary.comment.entity.CommentTagEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentTagMapper {
    CommentTagMapper INSTANCE = Mappers.getMapper(CommentTagMapper.class);


    CommentTagCompositeKey userAndCommentToCommentTagCompositeKey(Long userId, Long commentId);

    @Mapping(target = "id", source = "commentTagCompositeKey")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "comment", source = "comment")
    CommentTagEntity commentCreateRequestToEntity(CommentTagCompositeKey commentTagCompositeKey, UserEntity user, CommentEntity comment);

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "userName", source = "user.userName")
    CommentUserTagInfoDto entityToCommentUserInfoDto(UserEntity user);


}
