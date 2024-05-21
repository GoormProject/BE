package com.ttokttak.jellydiary.feed.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetUserInfoResponseDto {

    private Long userId;

    private String userName;

    private String userDesc;

    private String profileImg;

    private Long followerCount;

    private Long followingCount;

    private Boolean followStatus;

}
