package com.ttokttak.jellydiary.comment.dto;

import com.ttokttak.jellydiary.comment.entity.CommentEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "diaryPost", source = "diaryPost")
    CommentEntity commentCreateRequestToEntity(CommentCreateRequestDto dto, UserEntity user, DiaryPostEntity diaryPost);

    @Mapping(target = "userId", source = "userEntity.userId")
    @Mapping(target = "userName", source = "userEntity.userName")
    @Mapping(target = "userProfileImg", source = "userEntity.profileImg")
    @Mapping(target = "commentId", source = "comment.commentId")
    @Mapping(target = "commentContent", source = "comment.commentContent")
    @Mapping(target = "userTag", source = "userTagInfoDtos")
    CommentCreateCommentInfoDto entityAndDtoToCommentInfoDto(UserEntity userEntity, CommentEntity comment, Set<CommentUserTagInfoDto> userTagInfoDtos);

    @Mapping(target = "commentId", ignore = true)
    @Mapping(target = "commentContent", source = "dto.commentContent")
    @Mapping(target = "parent", source = "comment")
    @Mapping(target = "isDeleted", constant = "false")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "diaryPost", source = "diaryPost")
    CommentEntity replyCommentCreateRequestToEntity(CommentCreateRequestDto dto, UserEntity user, DiaryPostEntity diaryPost, CommentEntity comment);

//    @Mapping(target = "userId", source = "userEntity.userId")
//    @Mapping(target = "userName", source = "userEntity.userName")
//    @Mapping(target = "userProfileImg", source = "userEntity.profileImg")
//    @Mapping(target = "commentId", source = "comment.commentId")
//    @Mapping(target = "commentContent", source = "comment.commentContent")
//    @Mapping(target = "userTag", source = "userTagInfoDtos")
//    CommentCreateCommentInfoDto entityAndDtoToCommentInfoDto(UserEntity userEntity, CommentEntity comment, Set<CommentUserTagInfoDto> userTagInfoDtos);

    @Mapping(target = "comment", source = "createCommentInfoDto")
    CommentCreateResponseDto dtoToCommentCreateResponseDto(Long postId, CommentCreateCommentInfoDto createCommentInfoDto);

    @Mapping(target = "reply", source = "createReplyCommentInfoDto")
    ReplyCommentCreateResponseDto dtoToReplyCommentCreateResponseDto(Long parentId, CommentCreateCommentInfoDto createReplyCommentInfoDto);

    @Mapping(target = "commentId", source = "comment.commentId")
    @Mapping(target = "isDeleted", source = "comment.isDeleted")
    CommentDeleteResponseDto entityToCommentDeleteResponseDto(CommentEntity comment);

    @Mapping(target = "comments", source = "commentInfoDtos")
    CommentGetListResponseDto dtoToCommentGetListResponseDto(Long postId, List<CommentCreateCommentInfoDto> commentInfoDtos);

    @Mapping(target = "replies", source = "commentInfoDtos")
    ReplyCommentGetListResponseDto dtoToReplyCommentGetListResponseDto(Long commentId, List<CommentCreateCommentInfoDto> commentInfoDtos);


}
