package com.ttokttak.jellydiary.comment.repository;

import com.ttokttak.jellydiary.comment.entity.CommentTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentTagRepository extends JpaRepository<CommentTagEntity, Long> {
}
