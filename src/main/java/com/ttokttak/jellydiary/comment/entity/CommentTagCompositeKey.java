package com.ttokttak.jellydiary.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
public class CommentTagCompositeKey implements Serializable {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long commentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentTagCompositeKey that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(commentId, that.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, commentId);
    }

    @Builder
    public CommentTagCompositeKey(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}
