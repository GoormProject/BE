package com.ttokttak.jellydiary.diarypost.repository;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryPostImgRepository extends JpaRepository<DiaryPostEntity, Long> {

}

