package com.ttokttak.jellydiary.follow;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class FollowCompositeKey implements Serializable {
    @Column(nullable = false)
    private Long followRequestId;

    @Column(nullable = false)
    private Long followResponseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowCompositeKey that)) return false;
        return Objects.equals(followRequestId, that.followRequestId) && Objects.equals(followResponseId, that.followResponseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followRequestId, followResponseId);
    }
}
