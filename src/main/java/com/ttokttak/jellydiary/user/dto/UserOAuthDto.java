package com.ttokttak.jellydiary.user.dto;

import com.ttokttak.jellydiary.user.entity.Authority;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import lombok.Getter;

@Getter
public class UserOAuthDto {
    private Long userId;
    private String userName;
    private Authority authority;

    public UserOAuthDto(UserEntity userEntity){
        this.userId = userEntity.getUserId();
        this.userName = userEntity.getUserName();
        this.authority = userEntity.getAuthority();
    }

    public UserOAuthDto(Long userId, String userName,Authority authority){
        this.userId = userId;
        this.userName = userName;
        this.authority = authority;
    }
}
