package com.ttokttak.jellydiary.comment.service;

import com.ttokttak.jellydiary.comment.dto.*;
import com.ttokttak.jellydiary.comment.entity.CommentEntity;
import com.ttokttak.jellydiary.comment.entity.CommentTagCompositeKey;
import com.ttokttak.jellydiary.comment.entity.CommentTagEntity;
import com.ttokttak.jellydiary.comment.repository.CommentRepository;
import com.ttokttak.jellydiary.comment.repository.CommentTagRepository;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserRoleEnum;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diary.repository.DiaryUserRepository;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.notification.entity.NotificationSettingEntity;
import com.ttokttak.jellydiary.notification.entity.NotificationType;
import com.ttokttak.jellydiary.notification.repository.NotificationSettingRepository;
import com.ttokttak.jellydiary.notification.service.NotificationServiceImpl;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final DiaryProfileRepository diaryProfileRepository;
    private final DiaryPostRepository diaryPostRepository;
    private final DiaryUserRepository diaryUserRepository;
    private final CommentRepository commentRepository;
    private final CommentTagRepository commentTagRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final CommentMapper commentMapper;
    private final CommentTagMapper commentTagMapper;
    private final NotificationServiceImpl notificationServiceImpl;

    @Transactional
    @Override
    public ResponseDto<?> createComment(Long postId, CommentCreateRequestDto commentCreateRequestDto, CustomOAuth2User customOAuth2User) {
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

        Optional<DiaryUserEntity> dbDiaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity);
        DiaryUserEntity diaryUserEntity;
        if (!diaryPostEntity.getIsPublic()) { //게시글이 비공개 게시글인 경우 구독자와 일반 사용자는 해당 게시글에 접근하지 못한다.

            if (dbDiaryUserEntity.isPresent()) { //dbDiaryUserEntity의 값이 존재한다면 구독자인지 여부 확인 후 예외처리
                diaryUserEntity = dbDiaryUserEntity.get();

                if (diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.SUBSCRIBE) {
                    throw new CustomException(SUBSCRIBE_DOES_NOT_HAVE_PERMISSION_TO_READ_PRIVATE);
                }
            } else { //dbDiaryUserEntity의 값이 존재하지 않는다면 해당 다이어리와 게시물에 연관이 없는 일반 사용자이므로 이에 따른 예외처리
                throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_READ_PRIVATE);
            }
        }

        //CommentEntity 생성 후 저장
        CommentEntity commentEntity = commentMapper.commentCreateRequestToEntity(commentCreateRequestDto, userEntity, diaryPostEntity);
        commentRepository.save(commentEntity);

        Set<CommentUserTagInfoDto> commentUserTagInfoDtos = new HashSet<>();
        //CommentTag entity 생성 후 저장
        for (Long userId : commentCreateRequestDto.getUserTag()) {
            UserEntity tagUserEntity = userRepository.findById(userId).orElseThrow(
                    () -> new CustomException(USER_TAG_NOT_FOUND)
            );
            if (userId.equals(userEntity.getUserId())) {
                throw new CustomException(YOU_CANNOT_TAG_YOURSELF);
            }

            Optional<DiaryUserEntity> dbTagDiaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, tagUserEntity);
            DiaryUserEntity tagDiaryUserEntity;
            if (!diaryPostEntity.getIsPublic()) { //게시글이 비공개 게시글인 경우 구독자와 일반 사용자는 해당 게시글에 접근하지 못한다.

                if (dbTagDiaryUserEntity.isPresent()) { //dbDiaryUserEntity의 값이 존재한다면 구독자인지 여부 확인 후 예외처리
                    tagDiaryUserEntity = dbDiaryUserEntity.get();

                    if (tagDiaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.SUBSCRIBE) {
                        throw new CustomException(SUBSCRIBE_DOES_NOT_HAVE_PERMISSION_TO_READ_PRIVATE_AND_COMMENT);
                    }
                } else { //dbDiaryUserEntity의 값이 존재하지 않는다면 해당 다이어리와 게시물에 연관이 없는 일반 사용자이므로 이에 따른 예외처리
                    throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_READ_PRIVATE_POST_AND_COMMENT);
                }
            }

            CommentTagCompositeKey commentTagCompositeKey = commentTagMapper.userAndCommentToCommentTagCompositeKey(tagUserEntity.getUserId(), commentEntity.getCommentId());
            CommentTagEntity commentTagEntity = commentTagMapper.commentCreateRequestToEntity(commentTagCompositeKey, tagUserEntity, commentEntity);
            commentTagRepository.save(commentTagEntity);

            CommentUserTagInfoDto commentUserTagInfoDto = commentTagMapper.entityToCommentUserInfoDto(tagUserEntity);
            commentUserTagInfoDtos.add(commentUserTagInfoDto);
        }

        CommentCreateCommentInfoDto commentCreateCommentInfoDto = commentMapper.entityAndDtoToCommentInfoDto(userEntity, commentEntity, commentUserTagInfoDtos);
        CommentCreateResponseDto commentCreateResponseDto = commentMapper.dtoToCommentCreateResponseDto(diaryPostEntity.getPostId(), commentCreateCommentInfoDto);

        //게시물 생성자에게 알림 발송
        Optional<NotificationSettingEntity> notificationSettingEntity = notificationSettingRepository.findByUser(diaryPostEntity.getUser());
        if (notificationSettingEntity.isPresent()) {
            if (diaryPostEntity.getUser().getNotificationSetting() && notificationSettingEntity.get().getPostComment()) {
                Long receiverId = diaryPostEntity.getUser().getUserId();
                notificationServiceImpl.send(userEntity.getUserId(), receiverId, NotificationType.COMMENT_CREATE_REQUEST, NotificationType.COMMENT_CREATE_REQUEST.makeContent(userEntity.getUserName()), commentEntity.getCommentId());
            }
        }

        //태그된 사용자에게 알림 발송
        Set<CommentTagEntity> dbCommentTagEntities = commentTagRepository.findAllByComment(commentEntity);
        for (CommentTagEntity dbCommentTagEntity : dbCommentTagEntities) {
            Optional<NotificationSettingEntity> notificationSettingEntityTagUser = notificationSettingRepository.findByUser(dbCommentTagEntity.getUser());
            if (notificationSettingEntityTagUser.isPresent()) {
                if (!dbCommentTagEntity.getUser().getNotificationSetting() || !notificationSettingEntityTagUser.get().getCommentTag()) {
                    continue;
                }
            }
            Long tagReceiverId = dbCommentTagEntity.getUser().getUserId();
            notificationServiceImpl.send(userEntity.getUserId(), tagReceiverId, NotificationType.COMMENT_MENTION_CREATE_REQUEST, NotificationType.COMMENT_MENTION_CREATE_REQUEST.makeContent(userEntity.getUserName()), commentEntity.getCommentId());
        }

        return ResponseDto.builder()
                .statusCode(CREATE_COMMENT_SUCCESS.getHttpStatus().value())
                .message(CREATE_COMMENT_SUCCESS.getDetail())
                .data(commentCreateResponseDto)
                .build();
    }

    @Transactional
    @Override
    public ResponseDto<?> createReplyComment(Long postId, Long commentId, CommentCreateRequestDto commentCreateRequestDto, CustomOAuth2User customOAuth2User) {
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

        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
        if (commentEntity.getParent() != null) { //댓글의 depth는 1개까지이다. parent가 null이면 댓글, null이 아니면 답글이라는 의미로, 답글일 때는 "댓글에만 답글을 작성할 수 있다"는 예외를 발생.
            throw new CustomException(CANNOT_REPLY_COMMENT_TO_REPLIES);
        }

        if (!Objects.equals(diaryPostEntity.getPostId(), commentEntity.getDiaryPost().getPostId())) {
            throw new CustomException(COMMENT_AND_POST_DO_DOT_MATCH);
        }

        Optional<DiaryUserEntity> dbDiaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity);
        DiaryUserEntity diaryUserEntity;
        if (!diaryPostEntity.getIsPublic()) { //게시글이 비공개 게시글인 경우 구독자와 일반 사용자는 해당 게시글에 접근하지 못한다.

            if (dbDiaryUserEntity.isPresent()) { //dbDiaryUserEntity의 값이 존재한다면 구독자인지 여부 확인 후 예외처리
                diaryUserEntity = dbDiaryUserEntity.get();

                if (diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.SUBSCRIBE) {
                    throw new CustomException(SUBSCRIBE_DOES_NOT_HAVE_PERMISSION_TO_READ_PRIVATE);
                }
            } else { //dbDiaryUserEntity의 값이 존재하지 않는다면 해당 다이어리와 게시물에 연관이 없는 일반 사용자이므로 이에 따른 예외처리
                throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_READ_PRIVATE);
            }
        }

        //CommentEntity 생성 후 저장
        CommentEntity ReplyCommentEntity = commentMapper.replyCommentCreateRequestToEntity(commentCreateRequestDto, userEntity, diaryPostEntity, commentEntity);
        commentRepository.save(ReplyCommentEntity);

        Set<CommentUserTagInfoDto> commentUserTagInfoDtos = new HashSet<>();
        //CommentTag entity 생성 후 저장
        for (Long userId : commentCreateRequestDto.getUserTag()) {
            UserEntity tagUserEntity = userRepository.findById(userId).orElseThrow(
                    () -> new CustomException(USER_TAG_NOT_FOUND)
            );
            if (userId.equals(userEntity.getUserId())) {
                throw new CustomException(YOU_CANNOT_TAG_YOURSELF);
            }

            Optional<DiaryUserEntity> dbTagDiaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, tagUserEntity);
            DiaryUserEntity tagDiaryUserEntity;
            if (!diaryPostEntity.getIsPublic()) { //게시글이 비공개 게시글인 경우 구독자와 일반 사용자는 해당 게시글에 접근하지 못한다.

                if (dbTagDiaryUserEntity.isPresent()) { //dbDiaryUserEntity의 값이 존재한다면 구독자인지 여부 확인 후 예외처리
                    tagDiaryUserEntity = dbDiaryUserEntity.get();

                    if (tagDiaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.SUBSCRIBE) {
                        throw new CustomException(SUBSCRIBE_DOES_NOT_HAVE_PERMISSION_TO_READ_PRIVATE_AND_COMMENT);
                    }
                } else { //dbDiaryUserEntity의 값이 존재하지 않는다면 해당 다이어리와 게시물에 연관이 없는 일반 사용자이므로 이에 따른 예외처리
                    throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_READ_PRIVATE_POST_AND_COMMENT);
                }
            }

            CommentTagCompositeKey commentTagCompositeKey = commentTagMapper.userAndCommentToCommentTagCompositeKey(tagUserEntity.getUserId(), ReplyCommentEntity.getCommentId());
            CommentTagEntity commentTagEntity = commentTagMapper.commentCreateRequestToEntity(commentTagCompositeKey, tagUserEntity, ReplyCommentEntity);
            commentTagRepository.save(commentTagEntity);

            CommentUserTagInfoDto commentUserTagInfoDto = commentTagMapper.entityToCommentUserInfoDto(tagUserEntity);
            commentUserTagInfoDtos.add(commentUserTagInfoDto);
        }

        CommentCreateCommentInfoDto replyCommentCreateCommentInfoDto = commentMapper.entityAndDtoToCommentInfoDto(userEntity, ReplyCommentEntity, commentUserTagInfoDtos);
        ReplyCommentCreateResponseDto replyCommentCreateResponseDto = commentMapper.dtoToReplyCommentCreateResponseDto(commentId, replyCommentCreateCommentInfoDto);

        //댓글 생성자에게 알림 발송
        Optional<NotificationSettingEntity> notificationSettingEntity = notificationSettingRepository.findByUser(commentEntity.getUser());
        if (notificationSettingEntity.isPresent()) {
            if (commentEntity.getUser().getNotificationSetting() && notificationSettingEntity.get().getPostComment()) {
                Long receiverId = commentEntity.getUser().getUserId();
                notificationServiceImpl.send(userEntity.getUserId(), receiverId, NotificationType.REPLY_COMMENT_CREATE_REQUEST, NotificationType.REPLY_COMMENT_CREATE_REQUEST.makeContent(userEntity.getUserName()), commentEntity.getCommentId());
            }
        }

        //태그된 사용자에게 알림 발송
        Set<CommentTagEntity> dbCommentTagEntities = commentTagRepository.findAllByComment(commentEntity);
        for (CommentTagEntity dbCommentTagEntity : dbCommentTagEntities) {
            Optional<NotificationSettingEntity> notificationSettingEntityTagUser = notificationSettingRepository.findByUser(dbCommentTagEntity.getUser());
            if (notificationSettingEntityTagUser.isPresent()) {
                if (!dbCommentTagEntity.getUser().getNotificationSetting() || !notificationSettingEntityTagUser.get().getCommentTag()) {
                    continue;
                }
            }

            Long tagReceiverId = dbCommentTagEntity.getUser().getUserId();
            notificationServiceImpl.send(userEntity.getUserId(), tagReceiverId, NotificationType.COMMENT_MENTION_CREATE_REQUEST, NotificationType.COMMENT_MENTION_CREATE_REQUEST.makeContent(userEntity.getUserName()), commentEntity.getCommentId());
        }

        return ResponseDto.builder()
                .statusCode(REPLY_CREATE_COMMENT_SUCCESS.getHttpStatus().value())
                .message(REPLY_CREATE_COMMENT_SUCCESS.getDetail())
                .data(replyCommentCreateResponseDto)
                .build();
    }

    @Transactional
    @Override
    public ResponseDto<?> getCommentList(Long postId, CustomOAuth2User customOAuth2User) {
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

        Optional<DiaryUserEntity> dbDiaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity);
        DiaryUserEntity diaryUserEntity;
        if (!diaryPostEntity.getIsPublic()) { //게시글이 비공개 게시글인 경우 구독자와 일반 사용자는 해당 게시글에 접근하지 못한다.

            if (dbDiaryUserEntity.isPresent()) { //dbDiaryUserEntity의 값이 존재한다면 구독자인지 여부 확인 후 예외처리
                diaryUserEntity = dbDiaryUserEntity.get();

                if (diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.SUBSCRIBE) {
                    throw new CustomException(SUBSCRIBE_DOES_NOT_HAVE_PERMISSION_TO_READ_PRIVATE);
                }
            } else { //dbDiaryUserEntity의 값이 존재하지 않는다면 해당 다이어리와 게시물에 연관이 없는 일반 사용자이므로 이에 따른 예외처리
                throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_READ_PRIVATE);
            }
        }

        List<CommentEntity> commentEntities = commentRepository.findAllByDiaryPostAndParent(diaryPostEntity);
        List<CommentGetCommentInfoDto> commentGetCommentInfoDto = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntities) {
            Set<CommentTagEntity> commentTagEntities = commentTagRepository.findAllByComment(commentEntity);
            Set<CommentUserTagInfoDto> commentUserTagInfoDtos = commentTagEntities.stream().map(commentTagEntity -> commentTagMapper.entityToCommentUserInfoDto(commentTagEntity.getUser())).collect(Collectors.toSet());

            List<CommentEntity> replyComments = commentRepository.findAllByDiaryPostAndParent(diaryPostEntity, commentEntity);
            Long replyCount = Long.valueOf(replyComments.size());
            CommentGetCommentInfoDto commentCreateCommentInfoDto = commentMapper.entityAndDtoToCommentGetInfoDto(commentEntity.getUser(), commentEntity, commentUserTagInfoDtos, replyCount);
            commentGetCommentInfoDto.add(commentCreateCommentInfoDto);

        }

        CommentGetListResponseDto commentGetListResponseDto = commentMapper.dtoToCommentGetListResponseDto(postId, commentGetCommentInfoDto);

        return ResponseDto.builder()
                .statusCode(GET_LIST_POST_COMMENT.getHttpStatus().value())
                .message(GET_LIST_POST_COMMENT.getDetail())
                .data(commentGetListResponseDto)
                .build();
    }

    @Transactional
    @Override
    public ResponseDto<?> getReplyCommentList(Long postId, Long commentId, CustomOAuth2User customOAuth2User) {
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

        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
        if (commentEntity.getParent() != null) { //댓글의 depth는 1개까지이다. parent가 null이면 댓글, null이 아니면 답글이라는 의미로, 답글일 때는 "댓글에만 답글을 작성할 수 있다"는 예외를 발생.
            throw new CustomException(THE_COMMNET_YOU_REQUESTED_IS_A_REPLY);
        }

        if (!Objects.equals(diaryPostEntity.getPostId(), commentEntity.getDiaryPost().getPostId())) {
            throw new CustomException(COMMENT_AND_POST_DO_DOT_MATCH);
        }

        Optional<DiaryUserEntity> dbDiaryUserEntity = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, userEntity);
        DiaryUserEntity diaryUserEntity;
        if (!diaryPostEntity.getIsPublic()) { //게시글이 비공개 게시글인 경우 구독자와 일반 사용자는 해당 게시글에 접근하지 못한다.

            if (dbDiaryUserEntity.isPresent()) { //dbDiaryUserEntity의 값이 존재한다면 구독자인지 여부 확인 후 예외처리
                diaryUserEntity = dbDiaryUserEntity.get();

                if (diaryUserEntity.getDiaryRole() == DiaryUserRoleEnum.SUBSCRIBE) {
                    throw new CustomException(SUBSCRIBE_DOES_NOT_HAVE_PERMISSION_TO_READ_PRIVATE);
                }
            } else { //dbDiaryUserEntity의 값이 존재하지 않는다면 해당 다이어리와 게시물에 연관이 없는 일반 사용자이므로 이에 따른 예외처리
                throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_READ_PRIVATE);
            }
        }

        List<CommentEntity> replyCommentEntities = commentRepository.findAllByDiaryPostAndParent(diaryPostEntity, commentEntity);
        List<CommentCreateCommentInfoDto> commentCreateCommentInfoDtos = new ArrayList<>();
        for (CommentEntity replyComment : replyCommentEntities) {
            Set<CommentTagEntity> commentTagEntities = commentTagRepository.findAllByComment(replyComment);
            Set<CommentUserTagInfoDto> commentUserTagInfoDtos = commentTagEntities.stream().map(commentTagEntity -> commentTagMapper.entityToCommentUserInfoDto(commentTagEntity.getUser())).collect(Collectors.toSet());

            CommentCreateCommentInfoDto commentCreateCommentInfoDto = commentMapper.entityAndDtoToCommentInfoDto(replyComment.getUser(), replyComment, commentUserTagInfoDtos);
            commentCreateCommentInfoDtos.add(commentCreateCommentInfoDto);

        }

        ReplyCommentGetListResponseDto replyCommentGetListResponseDto = commentMapper.dtoToReplyCommentGetListResponseDto(commentId, commentCreateCommentInfoDtos);

        return ResponseDto.builder()
                .statusCode(GET_LIST_POST_REPLY_COMMENT.getHttpStatus().value())
                .message(GET_LIST_POST_REPLY_COMMENT.getDetail())
                .data(replyCommentGetListResponseDto)
                .build();
    }

    @Transactional
    @Override
    public ResponseDto<?> deleteComment(Long postId, Long commentId, CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        DiaryPostEntity diaryPostEntity = diaryPostRepository.findByIdWithDiaryProfile(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
        if(diaryPostEntity.getIsDeleted())
            throw new CustomException(POST_ALREADY_DELETED);

        DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(diaryPostEntity.getDiaryProfile().getDiaryId())
                .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));
        if(diaryProfileEntity.getIsDiaryDeleted())
            throw new CustomException(DIARY_ALREADY_DELETED);

        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        if (!Objects.equals(diaryPostEntity.getPostId(), commentEntity.getDiaryPost().getPostId())) {
            throw new CustomException(COMMENT_AND_POST_DO_DOT_MATCH);
        }

        if (commentEntity.getUser() != userEntity) {
            throw new CustomException(YOU_DO_NOT_HAVE_PERMISSION_TO_DELETE_COMMENT);
        }

        List<CommentEntity> childComments = commentRepository.findByParent(commentEntity);
        commentRepository.deleteAll(childComments);
        commentRepository.delete(commentEntity);

        return ResponseDto.builder()
                .statusCode(DELETE_COMMENT_SUCCESS.getHttpStatus().value())
                .message(DELETE_COMMENT_SUCCESS.getDetail())
                .build();
    }
}
