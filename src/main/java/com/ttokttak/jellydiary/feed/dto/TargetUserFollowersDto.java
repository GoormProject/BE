package com.ttokttak.jellydiary.feed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetUserFollowersDto {

    private Long userId;

    private String userName;

    private String userDesc;

    private String profileImg;

}
