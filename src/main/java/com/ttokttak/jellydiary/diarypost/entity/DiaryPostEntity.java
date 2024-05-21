package com.ttokttak.jellydiary.diarypost.entity;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE diary_post SET is_deleted = true WHERE post_id = ?")
@Table(name = "diary_post")
public class DiaryPostEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String postTitle;

    @Column(nullable = false)
    private LocalDate postDate;

    @Column
    private String meal;

    @Column String snack;

    @Column
    private String water;

    @Column
    private String walk;

    @Column
    private String toiletRecord;

    @Column
    private String shower;

    @Column
    private String weight;

    @Column
    private String specialNote;

    @Enumerated(EnumType.STRING)
    @Column
    private WeatherEnum weather;

    @Column(nullable = false)
    private String postContent;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private DiaryProfileEntity diaryProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Builder
    public DiaryPostEntity(Long postId, String postTitle, LocalDate postDate, String meal, String snack, String water, String walk, String toiletRecord, String shower, String weight, String specialNote, WeatherEnum weather, String postContent, Boolean isPublic, Boolean isDeleted, DiaryProfileEntity diaryProfile, UserEntity user) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postDate = postDate;
        this.meal = meal;
        this.snack = snack;
        this.water = water;
        this.walk = walk;
        this.toiletRecord = toiletRecord;
        this.shower = shower;
        this.weight = weight;
        this.specialNote = specialNote;
        this.weather = weather;
        this.postContent = postContent;
        this.isPublic = isPublic;
        this.isDeleted = isDeleted;
        this.diaryProfile = diaryProfile;
        this.user = user;
    }
}
