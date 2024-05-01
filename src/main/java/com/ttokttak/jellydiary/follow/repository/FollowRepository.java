package com.ttokttak.jellydiary.follow.repository;

import com.ttokttak.jellydiary.follow.entity.FollowCompositeKey;
import com.ttokttak.jellydiary.follow.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<FollowEntity, FollowCompositeKey> {

    Long countByIdFollowResponseId(Long targetUserId);  //팔로우 수

    Long countByIdFollowRequestId(Long targetUserId); //팔로워 수

}
