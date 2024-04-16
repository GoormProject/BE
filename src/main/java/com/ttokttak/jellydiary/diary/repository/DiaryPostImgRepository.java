package com.ttokttak.jellydiary.diary.repository;

import com.ttokttak.jellydiary.diary.entity.DiaryPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DiaryPostImgRepository extends JpaRepository<DiaryPostEntity, Long> {

}

