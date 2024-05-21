package com.ttokttak.jellydiary.user.dto.oauth2;

import com.ttokttak.jellydiary.user.entity.ProviderType;

import java.util.Map;

public class KakaoResponse implements OAuth2Response{
    private final Map<String, Object> attribute;

    /**
     * {
     *      "id": "123123123", "kakao_account": { "profile": {"nickname": "개발자유미"}, "email": "user@example.com"}
     * }
     **/
    public KakaoResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public ProviderType getProvider() {
        return ProviderType.KAKAO;
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attribute.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return profile.get("nickname").toString();
    }

}