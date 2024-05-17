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
    private String id;

    private Long userId;

    private String userName;

    private String refreshToken;

    @TimeToLive
    private Long expiration;

    @Builder
    public RefreshTokenEntity(Long userId, String userName, String refreshToken, Long expiration) {
        this.id = userId + ":" + refreshToken;
        this.userId = userId;
        this.userName = userName;
        this.refreshToken = refreshToken;
        this.expiration = expiration;
    }
}
