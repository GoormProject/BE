package com.ttokttak.jellydiary.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserNotificationSettingResponseDto {
    private Boolean notificationSetting;
    private Boolean postLike;
    private Boolean postComment;
    private Boolean postCreated;
    private Boolean commentTag;
    private Boolean newFollower;
    private Boolean dm;

    @Builder
    public UserNotificationSettingResponseDto(Boolean notificationSetting, Boolean postLike, Boolean postComment, Boolean postCreated, Boolean commentTag, Boolean newFollower, Boolean dm) {
        this.notificationSetting = notificationSetting;
        this.postLike = postLike;
        this.postComment = postComment;
        this.postCreated = postCreated;
        this.commentTag = commentTag;
        this.newFollower = newFollower;
        this.dm = dm;
    }
}
