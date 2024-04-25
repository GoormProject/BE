package com.ttokttak.jellydiary.user.dto;

import com.ttokttak.jellydiary.user.dto.oauth2.OAuth2Response;
import com.ttokttak.jellydiary.user.entity.Authority;
import com.ttokttak.jellydiary.user.entity.ProviderType;
import com.ttokttak.jellydiary.user.entity.UserStateEnum;
import lombok.Getter;

@Getter
public class UserCreateDto {
    private String oauthId;
    private ProviderType providerType;
    private String userEmail;
    private String userName;
    private Authority authority;
    private UserStateEnum userState;
    private Boolean notificationSetting;

    public UserCreateDto(OAuth2Response oAuth2Response, String oauthId) {
        this.oauthId = oauthId;
        this.providerType = oAuth2Response.getProvider();
        this.userEmail = oAuth2Response.getEmail();
        this.userName = oAuth2Response.getName();
        this.authority = Authority.USER;
        this.userState = UserStateEnum.ACTIVE;
        this.notificationSetting = true;
    }
}
