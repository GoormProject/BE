package com.ttokttak.jellydiary.diary.repository;

import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryUserRepository extends JpaRepository<DiaryUserEntity, Long> {
}
