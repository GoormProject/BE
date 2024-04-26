package com.ttokttak.jellydiary.diary.repository;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryUserRepository extends JpaRepository<DiaryUserEntity, Long> {

    DiaryUserEntity findByDiaryIdAndUserId(DiaryProfileEntity diaryId, UserEntity userId);

}
