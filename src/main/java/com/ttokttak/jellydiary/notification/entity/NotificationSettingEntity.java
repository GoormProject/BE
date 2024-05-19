package com.ttokttak.jellydiary.notification.entity;

import com.ttokttak.jellydiary.user.entity.Authority;
import com.ttokttak.jellydiary.user.entity.ProviderType;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.entity.UserStateEnum;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notification_setting")
public class NotificationSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;  // UserEntity의 ID와 매핑

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId  // UserEntity의 ID를 이 엔티티의 ID로 사용
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    private Boolean postLike;

    @Column(nullable = false)
    private Boolean postComment;

    @Column(nullable = false)
    private Boolean post;

    @Column(nullable = false)
    private Boolean diary;

    @Column(nullable = false)
    private Boolean commentTag;

    @Column(nullable = false)
    private Boolean newFollower;

    @Column(nullable = false)
    private Boolean dm;

    @Builder
    public NotificationSettingEntity(UserEntity user, Boolean postLike, Boolean postComment, Boolean post, Boolean diary, Boolean commentTag, Boolean newFollower, Boolean dm) {
        this.user = user;
        this.postLike = postLike;
        this.postComment = postComment;
        this.post = post;
        this.diary = diary;
        this.commentTag = commentTag;
        this.newFollower = newFollower;
        this.dm = dm;
    }

    public void notificationsSettingUpdate(Boolean postLike, Boolean postComment, Boolean post, Boolean diary, Boolean commentTag, Boolean newFollower, Boolean dm) {
        this.postLike = postLike;
        this.postComment = postComment;
        this.post = post;
        this.diary = diary;
        this.commentTag = commentTag;
        this.newFollower = newFollower;
        this.dm = dm;
    }
}
