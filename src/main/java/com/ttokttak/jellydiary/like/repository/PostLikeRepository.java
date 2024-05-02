package com.ttokttak.jellydiary.like.repository;

import com.ttokttak.jellydiary.like.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {
}
