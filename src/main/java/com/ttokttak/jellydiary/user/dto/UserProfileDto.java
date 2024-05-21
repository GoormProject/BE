package com.ttokttak.jellydiary.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileDto {
    private Long userId;
    private String userName;
    private String userDesc;
    private String profileImg;
    private String authority;
    private String userState;
    private Boolean notificationSetting;
    private Boolean subscribe;
    private Boolean postLike;
    private Boolean postComment;
    private Boolean post;
    private Boolean diary;
    private Boolean commentTag;
    private Boolean newFollower;
    private Boolean dm;


    @Builder
    public UserProfileDto(Long userId, String userName, String userDesc, String profileImg, String authority, String userState, Boolean notificationSetting, Boolean subscribe, Boolean postLike, Boolean postComment, Boolean post, Boolean diary, Boolean commentTag, Boolean newFollower, Boolean dm) {
        this.userId = userId;
        this.userName = userName;
        this.userDesc = userDesc;
        this.profileImg = profileImg;
        this.authority = authority;
        this.userState = userState;
        this.notificationSetting = notificationSetting;
        this.subscribe = subscribe;
        this.postLike = postLike;
        this.postComment = postComment;
        this.post = post;
        this.diary = diary;
        this.commentTag = commentTag;
        this.newFollower = newFollower;
        this.dm = dm;
    }
}
