package com.ttokttak.jellydiary.diary.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "diary_profile")
public class DiaryProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    @Column(nullable = false)
    private String diaryName;

    private String diaryDescription;

    private String diaryProfileImage;

    @Column(nullable = false)
    private Boolean isDiaryDeleted;

    @Column
    private Long chatRoomId;

}
