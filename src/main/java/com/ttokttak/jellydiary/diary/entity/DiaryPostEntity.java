package com.ttokttak.jellydiary.diary.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Table(name = "diary_post")
public class DiaryPostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String postTitle;

    private String meal;

    private String water;

    private String walk;

    private String toilet_record;

    private String shower;

    private String weight;

    private String special_note;

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
    private Boolean isDelete;

    @Column(nullable = false)
    private Long diaryId;

    @Column(nullable = false)
    private Long userId;
}
