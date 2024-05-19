package com.ttokttak.jellydiary.user.entity;

import com.ttokttak.jellydiary.notification.entity.NotificationSettingEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String oauthId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Column
    private String userEmail;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column
    private String userDesc;

    @Column
    private String profileImg;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStateEnum userState;

    @Column
    private Boolean notificationSetting;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private NotificationSettingEntity notificationSettings;

    @Builder
    public UserEntity(Long userId, String oauthId, ProviderType providerType, String userEmail, String userName, String userDesc, String profileImg, Authority authority, UserStateEnum userState, Boolean notificationSetting) {
        this.userId = userId;
        this.oauthId = oauthId;
        this.providerType = providerType;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userDesc = userDesc;
        this.profileImg = profileImg;
        this.authority = authority;
        this.userState = userState;
        this.notificationSetting = notificationSetting;
    }

    @Builder
    public UserEntity(String oauthId, ProviderType providerType, String userEmail, String userName, Authority authority, UserStateEnum userState, Boolean notificationSetting) {
        this.oauthId = oauthId;
        this.providerType = providerType;
        this.userEmail = userEmail;
        this.userName = userName;
        this.authority = authority;
        this.userState = userState;
        this.notificationSetting = notificationSetting;
    }

    // 유저 생성 이후 호출되는 콜백 메서드
    @PostPersist
    private void initializeNotificationSettings() {
        this.notificationSettings = new NotificationSettingEntity(this, true, true, true, true, true, true, true);
    }
    
    public void uploadProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void userProfileUpdate(String userName, String userDescription) {
        this.userName = userName;
        this.userDesc = userDescription;
    }

    public void userNotificationSettingUpdate(Boolean notificationSetting) {
        this.notificationSetting = notificationSetting;
    }
}