package com.ttokttak.jellydiary.comment.repository;

import com.ttokttak.jellydiary.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
