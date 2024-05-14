package com.ttokttak.jellydiary.diarypost.repository;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface DiaryPostRepositoryCustom {
    Slice<DiaryPostEntity> postOrderByCreatedAtDesc(UserEntity user, Pageable pageable);
}
