package com.ttokttak.jellydiary.user.dto;

import lombok.Getter;

@Getter
public class UserNotificationSettingRequestDto {
    private Boolean notificationSetting;
    private Boolean postLike;
    private Boolean postComment;
    private Boolean postCreated;
    private Boolean commentTag;
    private Boolean newFollower;
    private Boolean dm;
}
