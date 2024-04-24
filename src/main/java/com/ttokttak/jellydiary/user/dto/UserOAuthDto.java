package com.ttokttak.jellydiary.user.dto;

import com.ttokttak.jellydiary.user.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserOAuthDto {
    private Long userId;
    private String userName;
    private String authority;

    public UserOAuthDto(UserEntity userEntity){
        this.userId = userEntity.getUserId();
        this.userName = userEntity.getUserName();
        this.authority = String.valueOf(userEntity.getAuthority());
    }

    public UserOAuthDto(Long userId, String userName, String authority){
        this.userId = userId;
        this.userName = userName;
        this.authority = authority;
    }
}
