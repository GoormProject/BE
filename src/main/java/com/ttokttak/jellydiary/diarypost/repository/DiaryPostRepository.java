package com.ttokttak.jellydiary.diarypost.repository;

import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiaryPostRepository extends JpaRepository<DiaryPostEntity, Long>, DiaryPostRepositoryCustom {

    @Query("SELECT dp FROM DiaryPostEntity dp JOIN FETCH dp.diaryProfile WHERE dp.postId = :postId")
    Optional<DiaryPostEntity> findByIdWithDiaryProfile(@Param("postId") Long postId);

    @Query("select d from DiaryPostEntity d where d.diaryProfile = :diaryProfile and d.isPublic = :isPublic and d.isDeleted = :isDeleted")
    List<DiaryPostEntity> findAllByDiaryProfileAndIsPublicAAndIsDeleted(@Param("diaryProfile")DiaryProfileEntity diaryProfile, @Param("isPublic") Boolean isPublic, @Param("isDeleted") Boolean isDeleted);

    @Query("select d from DiaryPostEntity d where d.diaryProfile = :diaryProfile and d.isDeleted = :isDeleted")
    List<DiaryPostEntity> findAllByDiaryProfileAndIsDeleted(@Param("diaryProfile") DiaryProfileEntity diaryProfile, @Param("isDeleted") Boolean isDeleted);

    @Query("select d from DiaryPostEntity d where d.user = :targetUser and d.isDeleted = :isDeleted and (:loginUser = :targetUser or d.isPublic = true) order by d.createdAt desc")
    List<DiaryPostEntity> findFeedListForTargetUser(@Param("loginUser") UserEntity loginUser, @Param("targetUser") UserEntity targetUser, @Param("isDeleted") Boolean isDeleted);

}