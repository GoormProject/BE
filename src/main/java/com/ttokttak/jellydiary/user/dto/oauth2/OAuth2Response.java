package com.ttokttak.jellydiary.user.dto.oauth2;

import com.ttokttak.jellydiary.user.entity.ProviderType;

public interface OAuth2Response {

    // 제공자
    ProviderType getProvider();

    // 제공자에서 발급해주는 아이디(번호)
    String getProviderId();

    // 이름
    String getName();
}