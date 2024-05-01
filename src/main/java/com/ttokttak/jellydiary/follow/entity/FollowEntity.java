package com.ttokttak.jellydiary.follow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "follow")
public class FollowEntity {
    @EmbeddedId
    private FollowCompositeKey id;

    @Builder
    public FollowEntity(Long followRequestId, Long followResponseId) {
        this.id = new FollowCompositeKey(followRequestId, followResponseId);
    }

}
