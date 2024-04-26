package com.ttokttak.jellydiary.diarypost.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "diary_post_img")
public class DiaryPostImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImgId;

    @Column(nullable = false)
    private String imageLink;

    @Column(nullable = false)
    private Long postId;
}
