package com.ttokttak.jellydiary.user.dto.oauth2;

import com.ttokttak.jellydiary.user.entity.ProviderType;

import java.util.Map;

public class NaverResponse implements OAuth2Response{

    private final Map<String, Object> attribute;

    /**
     * {
     * 		resultcode=00, message=success, response={id=123123123, name=개발자유미}
     * }
     * 이중키 형식이기 때문에 response라는 키에 대해서 get을 한 다음에 값을 넣어 주어야함
     **/
    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public ProviderType getProvider() {
        return ProviderType.NAVER;
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}