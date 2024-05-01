package com.ttokttak.jellydiary.follow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
}
