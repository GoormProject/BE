package com.ttokttak.jellydiary.diarypost.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "diary_post_img")
public class DiaryPostImgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImgId;

    @Column(nullable = false)
    private String imageLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private DiaryPostEntity diaryPost;

    @Builder
    public DiaryPostImgEntity(Long postImgId, String imageLink, DiaryPostEntity diaryPost) {
        this.postImgId = postImgId;
        this.imageLink = imageLink;
        this.diaryPost = diaryPost;
    }
}
