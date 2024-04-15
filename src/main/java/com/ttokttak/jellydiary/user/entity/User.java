package com.ttokttak.jellydiary.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private Long oauthId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column(nullable = false)
    private String userName;

    @Column
    private String userDesc;

    @Column
    private String profileImg;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Authority userRole;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStateEnum userState;

    @Column
    private Boolean notificationSetting;

}
