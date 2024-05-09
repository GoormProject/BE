package com.ttokttak.jellydiary.util.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchUserListResponseDto {
    private Long userId;
    private String userName;
    private String profileImg;
    private Boolean isInvited;

    @Builder
    public SearchUserListResponseDto(Long userId, String userName, String profileImg, Boolean isInvited) {
        this.userId = userId;
        this.userName = userName;
        this.profileImg = profileImg;
        this.isInvited = isInvited;
    }
}
