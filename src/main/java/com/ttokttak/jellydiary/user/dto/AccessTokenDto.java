package com.ttokttak.jellydiary.user.dto;

import lombok.Getter;

@Getter
public class AccessTokenDto {
    private String authorization;

    public AccessTokenDto(String AccessToken) {
        this.authorization = AccessToken;
    }
}
