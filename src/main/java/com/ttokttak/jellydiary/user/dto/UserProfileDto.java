package com.ttokttak.jellydiary.user.dto;

import com.ttokttak.jellydiary.diarypost.dto.DiaryPostImgListResponseDto;
import com.ttokttak.jellydiary.user.entity.Authority;
import com.ttokttak.jellydiary.user.entity.UserStateEnum;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class UserProfileDto {
    private Long userId;
    private String userName;
    private String userDesc;
    private String profileImg;
    private String authority;
    private String userState;
    private Boolean notificationSetting;
    private Boolean postLike;
    private Boolean postComment;
    private Boolean postCreated;
    private Boolean commentTag;
    private Boolean newFollower;
    private Boolean dm;

    @Builder
    public UserProfileDto(Long userId, String userName, String userDesc, String profileImg, String authority, String userState, Boolean notificationSetting, Boolean postLike, Boolean postComment, Boolean postCreated, Boolean commentTag, Boolean newFollower, Boolean dm) {
        this.userId = userId;
        this.userName = userName;
        this.userDesc = userDesc;
        this.profileImg = profileImg;
        this.authority = authority;
        this.userState = userState;
        this.notificationSetting = notificationSetting;
        this.postLike = postLike;
        this.postComment = postComment;
        this.postCreated = postCreated;
        this.commentTag = commentTag;
        this.newFollower = newFollower;
        this.dm = dm;
    }
}
