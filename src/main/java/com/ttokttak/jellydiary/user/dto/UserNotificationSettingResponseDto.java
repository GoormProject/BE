package com.ttokttak.jellydiary.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserNotificationSettingResponseDto {
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
    public UserNotificationSettingResponseDto(Boolean notificationSetting, Boolean subscribe, Boolean postLike, Boolean postComment, Boolean post, Boolean diary, Boolean commentTag, Boolean newFollower, Boolean dm) {
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
