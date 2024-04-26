package com.ttokttak.jellydiary.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;

public interface DiaryProfileRepository extends JpaRepository<DiaryProfileEntity, Long> {

}
