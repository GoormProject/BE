package com.ttokttak.jellydiary.comment.repository;

import com.ttokttak.jellydiary.comment.entity.CommentEntity;
import com.ttokttak.jellydiary.comment.entity.CommentTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CommentTagRepository extends JpaRepository<CommentTagEntity, Long> {
    @Query("SELECT ct FROM CommentTagEntity ct JOIN FETCH ct.user WHERE ct.comment = :comment")
    Set<CommentTagEntity> findAllByComment(CommentEntity comment);
}
