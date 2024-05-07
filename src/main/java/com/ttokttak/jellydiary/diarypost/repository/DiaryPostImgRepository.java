package com.ttokttak.jellydiary.diarypost.repository;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiaryPostImgRepository extends JpaRepository<DiaryPostImgEntity, Long> {
    @Query("select d from DiaryPostImgEntity d where d.diaryPost = :diaryPost and d.isDeleted = :isDeleted")
    List<DiaryPostImgEntity> findAllByDiaryPostAndIsDeleted(@Param("diaryPost") DiaryPostEntity diaryPost, @Param("isDeleted") Boolean isDeleted);

    @Query("select d from DiaryPostImgEntity d where d.diaryPost = :diaryPost and d.isDeleted = :isDeleted order by d.postImgId asc")
    List<DiaryPostImgEntity> findByDiaryPostAndIsDeletedOrderByPostImgIdAsc(@Param("diaryPost") DiaryPostEntity diaryPost, @Param("isDeleted") Boolean isDeleted);

}

