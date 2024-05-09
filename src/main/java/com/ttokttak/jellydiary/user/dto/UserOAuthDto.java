package com.ttokttak.jellydiary.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserOAuthDto {
    private Long userId;
    private String userName;
    private String authority;

    @Builder
    public UserOAuthDto(Long userId, String userName, String authority){
        this.userId = userId;
        this.userName = userName;
        this.authority = authority;
    }
}
