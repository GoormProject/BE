package com.ttokttak.jellydiary.diarypost.repository;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryPostRepository extends JpaRepository<DiaryPostEntity, Long> {
    @Query("select d from DiaryPostEntity d where d.diaryProfile = :diaryProfile and d.isPublic = :isPublic and d.isDeleted = :isDeleted")
    List<DiaryPostEntity> findAllByDiaryProfileAndIsPublicAAndIsDeleted(@Param("diaryProfile")DiaryProfileEntity diaryProfile, @Param("isPublic") Boolean isPublic, @Param("isDeleted") Boolean isDeleted);

    @Query("select d from DiaryPostEntity d where d.diaryProfile = :diaryProfile and d.isDeleted = :isDeleted")
    List<DiaryPostEntity> findAllByDiaryProfileAndIsDeleted(@Param("diaryProfile") DiaryProfileEntity diaryProfile, @Param("isDeleted") Boolean isDeleted);
}