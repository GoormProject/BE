package com.ttokttak.jellydiary.like.repository;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.like.entity.PostLikeEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Long> {

    Optional<PostLikeEntity> findByUserAndDiaryPost(UserEntity user, DiaryPostEntity diaryPost);
}
