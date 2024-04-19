package com.ttokttak.jellydiary.user.dto.oauth2;

import com.ttokttak.jellydiary.user.dto.UserOAuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final UserOAuthDto userOAuthDto;

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    // Role값을 리턴해주는 메소드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userOAuthDto.getAuthority().name();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return userOAuthDto.getUserName();
    }

    public Long getUserId() {
        return userOAuthDto.getUserId();
    }

}

