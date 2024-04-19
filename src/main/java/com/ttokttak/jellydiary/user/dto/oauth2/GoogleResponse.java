package com.ttokttak.jellydiary.user.dto.oauth2;

import com.ttokttak.jellydiary.user.entity.ProviderType;

import java.util.Map;

public class GoogleResponse implements OAuth2Response{
    private final Map<String, Object> attribute;

    /**
     * {
     * 		resultcode=00, message=success, id=123123123, name=개발자유미
     * }
     * JSON 자체에 특정한 키에 대한 값이 있기 때문에 바로 생성자를 통해 필드를 초기화 하면됨
     **/
    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public ProviderType getProvider() {
        return ProviderType.GOOGLE;
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
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

