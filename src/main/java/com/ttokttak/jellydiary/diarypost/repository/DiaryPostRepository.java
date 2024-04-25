package com.ttokttak.jellydiary.diarypost.repository;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface DiaryPostRepository extends JpaRepository<DiaryPostEntity, Long> {

}