package com.ttokttak.jellydiary.follow.repository;

import com.ttokttak.jellydiary.follow.entity.FollowCompositeKey;
import com.ttokttak.jellydiary.follow.entity.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepository extends JpaRepository<FollowEntity, FollowCompositeKey> {

    Long countByIdFollowResponseId(Long targetUserId);

    Long countByIdFollowRequestId(Long targetUserId);

    List<FollowEntity> findByIdFollowResponseId(Long followResponseId);

    List<FollowEntity> findByIdFollowRequestId(Long followRequestId);

    boolean existsById(FollowCompositeKey followCompositeKey);

    @Query("SELECT COUNT(f) > 0 FROM FollowEntity f WHERE f.followRequest.id = :followRequestId AND f.followResponse.id = :followResponseId")
    boolean existsByFollowRequestAndFollowResponse(Long followRequestId, Long followResponseId);

}
