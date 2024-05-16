package com.ttokttak.jellydiary.user.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("RefreshToken")
public class RefreshTokenEntity {
    @Id
    private String refreshToken;

    private String username;

    @TimeToLive
    private Long expiration;

    @Builder
    public RefreshTokenEntity(String refreshToken, String username, Long expiration) {
        this.refreshToken = refreshToken;
        this.username = username;
        this.expiration = expiration;
    }
}
