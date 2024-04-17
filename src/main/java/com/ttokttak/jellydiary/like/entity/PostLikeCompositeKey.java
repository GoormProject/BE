package com.ttokttak.jellydiary.like.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeCompositeKey implements Serializable {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long diaryPostId;
}
