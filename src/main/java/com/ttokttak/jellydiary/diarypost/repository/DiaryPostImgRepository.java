package com.ttokttak.jellydiary.diarypost.repository;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryPostImgRepository extends JpaRepository<DiaryPostImgEntity, Long> {
    List<DiaryPostImgEntity> findAllByDiaryPost(DiaryPostEntity diaryPost);
}

