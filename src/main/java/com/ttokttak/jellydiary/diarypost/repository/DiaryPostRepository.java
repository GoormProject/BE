package com.ttokttak.jellydiary.diarypost.repository;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryPostRepository extends JpaRepository<DiaryPostEntity, Long> {
    @Query("select d from DiaryPostEntity d where d.diaryProfile = :diaryProfile and d.isPublic = :isPublic")
    List<DiaryPostEntity> findAllByDiaryProfileAndIsPublic(@Param("diaryProfile")DiaryProfileEntity diaryProfile, @Param("isPublic") Boolean isPublic);

    List<DiaryPostEntity> findAllByDiaryProfile(DiaryProfileEntity diaryProfileEntity);
}