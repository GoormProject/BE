package com.ttokttak.jellydiary.comment.repository;

import com.ttokttak.jellydiary.comment.entity.CommentEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT c FROM CommentEntity c LEFT JOIN FETCH c.user LEFT JOIN FETCH c.diaryPost WHERE c.diaryPost = :diaryPost AND c.parent IS NULL")
    List<CommentEntity> findAllByDiaryPostAndParent(@Param("diaryPost") DiaryPostEntity diaryPost);

    @Query("SELECT c FROM CommentEntity c LEFT JOIN FETCH c.user LEFT JOIN FETCH c.diaryPost WHERE c.diaryPost = :diaryPost AND c.parent = :parent AND c.parent IS NOT NULL")
    List<CommentEntity> findAllByDiaryPostAndParent(@Param("diaryPost") DiaryPostEntity diaryPost, @Param("parent") CommentEntity parent);

    List<CommentEntity> findByParent(CommentEntity comment);

}
