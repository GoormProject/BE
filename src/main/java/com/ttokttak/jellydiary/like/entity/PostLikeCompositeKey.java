package com.ttokttak.jellydiary.like.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeCompositeKey implements Serializable {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long diaryPostId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostLikeCompositeKey that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(diaryPostId, that.diaryPostId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, diaryPostId);
    }
}
