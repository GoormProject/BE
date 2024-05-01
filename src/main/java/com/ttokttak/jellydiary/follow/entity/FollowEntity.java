package com.ttokttak.jellydiary.follow.entity;

import com.ttokttak.jellydiary.user.entity.UserEntity;
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

    @ManyToOne
    @MapsId("followRequestId")
    @JoinColumn(name = "follow_request_id")
    private UserEntity followRequest;

    @ManyToOne
    @MapsId("followResponseId")
    @JoinColumn(name = "follow_response_id")
    private UserEntity followResponse;

    @Builder
    public FollowEntity(Long followRequestId, Long followResponseId, UserEntity followRequest, UserEntity followResponse) {
        this.id = new FollowCompositeKey(followRequestId, followResponseId);
        this.followRequest = followRequest;
        this.followResponse = followResponse;
    }

}
