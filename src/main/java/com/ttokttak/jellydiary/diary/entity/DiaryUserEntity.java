package com.ttokttak.jellydiary.diary.entity;

import com.ttokttak.jellydiary.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "diary_user")
public class DiaryUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryUserId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DiaryUserRoleEnum diaryRole;

    private Boolean isInvited;

    @ManyToOne
    @JoinColumn(name = "diary_id")
    private DiaryProfileEntity diaryId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @Builder
    public DiaryUserEntity(DiaryUserRoleEnum diaryRole, Boolean isInvited, DiaryProfileEntity diaryId, UserEntity userId) {
        this.diaryRole = diaryRole;
        this.isInvited = isInvited;
        this.diaryId = diaryId;
        this.userId = userId;
    }

    public void diaryUserRoleUpdate(DiaryUserRoleEnum diaryRole){
        this.diaryRole = diaryRole;
    }

    public void isInvitedUpdate(Boolean isInvited){
        this.isInvited = isInvited;
    }

}
