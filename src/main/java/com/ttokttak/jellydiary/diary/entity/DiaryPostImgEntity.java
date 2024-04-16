package com.ttokttak.jellydiary.diary.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "diary_post_img")
public class DiaryPostImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImgId;

    @Column(nullable = false, length = 200)
    private String imageLink;

    @Column(nullable = false)
    private Long postId;
}
