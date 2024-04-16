package com.ttokttak.jellydiary.diary.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "diary_profile")
public class DiaryProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    @Column(nullable = false, length = 50)
    private String diaryName;

    @Column(length = 300)
    private String diaryDescription;

    @Column(length = 50)
    private String diaryProfileImage;

    @Column(nullable = false)
    private Boolean isDiaryDeleted;

    @Column
    private Long chatRoomId;

}
