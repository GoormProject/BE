package com.ttokttak.jellydiary.diary.repository;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserRoleEnum;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiaryUserRepository extends JpaRepository<DiaryUserEntity, Long> {

    Optional<DiaryUserEntity> findByDiaryIdAndUserId(DiaryProfileEntity diaryId, UserEntity userId);

    List<DiaryUserEntity> findByDiaryIdAndDiaryRoleNot(DiaryProfileEntity diaryId, DiaryUserRoleEnum diaryRole);

    List<DiaryUserEntity> findByUserId(UserEntity userId);

    @Query("SELECT d FROM DiaryUserEntity d WHERE d.diaryId = :diaryId AND (d.diaryRole != 'SUBSCRIBE' OR (d.diaryRole = 'SUBSCRIBE' AND d.isInvited IS NOT NULL))")
    List<DiaryUserEntity> findByDiaryIdAndValidUsers(@Param("diaryId") DiaryProfileEntity diaryId);

    List<DiaryUserEntity> findAllByDiaryIdAndIsInvited(DiaryProfileEntity diaryProfile, Boolean isInvited);

}
