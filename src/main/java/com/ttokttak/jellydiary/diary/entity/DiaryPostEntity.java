package com.ttokttak.jellydiary.diary.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "diary_post")
public class DiaryPostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false, length = 70)
    private String postTitle;

    @Column(length = 50)
    private String meal;

    @Column(length = 50)
    private String water;

    @Column(length = 50)
    private String walk;

    @Column(length = 50)
    private String toiletRecord;

    @Column(length = 50)
    private String shower;

    @Column(length = 50)
    private String weight;

    @Column(length = 50)
    private String specialNote;

    @Column(length = 50)
    private String weather;

    @Column(columnDefinition = "TEXT")
    private String postContent;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private Long diaryId;

    @Column(nullable = false)
    private Long userId;
}
