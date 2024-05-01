package com.ttokttak.jellydiary.follow.repository;

import com.ttokttak.jellydiary.follow.entity.FollowCompositeKey;
import com.ttokttak.jellydiary.follow.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<FollowEntity, FollowCompositeKey> {

    Long countByIdFollowResponseId(Long targetUserId);

    Long countByIdFollowRequestId(Long targetUserId);

    List<FollowEntity> findByIdFollowResponseId(Long followResponseId);

}
