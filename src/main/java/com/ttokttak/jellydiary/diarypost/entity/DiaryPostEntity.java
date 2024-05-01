package com.ttokttak.jellydiary.diarypost.entity;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diarypost.dto.DiaryPostCreateRequestDto;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
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

    public void diaryPostUpdate(DiaryPostCreateRequestDto diaryPostCreateRequestDto) {
        this.postTitle = diaryPostCreateRequestDto.getPostTitle();
        this.postDate = LocalDate.parse(diaryPostCreateRequestDto.getPostDate(), DateTimeFormatter.ISO_DATE);
        this.meal = diaryPostCreateRequestDto.getMeal();
        this.snack = diaryPostCreateRequestDto.getSnack();
        this.water = diaryPostCreateRequestDto.getWater();
        this.walk = diaryPostCreateRequestDto.getWalk();
        this.toiletRecord = diaryPostCreateRequestDto.getToiletRecord();
        this.shower = diaryPostCreateRequestDto.getShower();
        this.weight = diaryPostCreateRequestDto.getWeight();
        this.specialNote = diaryPostCreateRequestDto.getSpecialNote();
        this.weather = WeatherEnum.valueOf(diaryPostCreateRequestDto.getWeather());
        this.postContent = diaryPostCreateRequestDto.getPostContent();
        this.isPublic = diaryPostCreateRequestDto.getIsPublic();
    }
}
