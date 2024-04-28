package com.ttokttak.jellydiary.diary.repository;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserRoleEnum;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryUserRepository extends JpaRepository<DiaryUserEntity, Long> {

    Optional<DiaryUserEntity> findByDiaryIdAndUserId(DiaryProfileEntity diaryId, UserEntity userId);

    List<DiaryUserEntity> findByDiaryIdAndDiaryRoleNot(DiaryProfileEntity diaryId, DiaryUserRoleEnum diaryRole);

    List<DiaryUserEntity> findByUserId(UserEntity userId);

}
