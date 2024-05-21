package com.ttokttak.jellydiary.diarypost.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserRoleEnum;
import com.ttokttak.jellydiary.diary.repository.DiaryUserRepository;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import com.ttokttak.jellydiary.user.entity.QUserEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ttokttak.jellydiary.diary.entity.QDiaryProfileEntity.diaryProfileEntity;
import static com.ttokttak.jellydiary.diary.entity.QDiaryUserEntity.diaryUserEntity;
import static com.ttokttak.jellydiary.diarypost.entity.QDiaryPostEntity.diaryPostEntity;
import static com.ttokttak.jellydiary.user.entity.QUserEntity.userEntity;

@RequiredArgsConstructor
public class DiaryPostRepositoryCustomImpl implements DiaryPostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<DiaryPostEntity> postOrderByCreatedAtDesc(UserEntity user, Pageable pageable, Long lastPostId) {
        BooleanExpression condition = diaryPostEntity.isPublic.isTrue();
        condition = condition.or(
                diaryUserEntity.diaryId.eq(diaryPostEntity.diaryProfile)
                        .and(diaryUserEntity.userId.eq(user))
                        .and(diaryUserEntity.diaryRole.ne(DiaryUserRoleEnum.SUBSCRIBE))
        );


        List<DiaryPostEntity> result = queryFactory.selectFrom(diaryPostEntity)
                .leftJoin(diaryPostEntity.user, userEntity).fetchJoin() // 사용자 엔티티를 함께 로딩합니다.
                .leftJoin(diaryPostEntity.diaryProfile, diaryProfileEntity).fetchJoin() // 다이어리 프로필 엔티티를 함께 로딩합니다.
                .leftJoin(diaryUserEntity)
                .on(diaryUserEntity.diaryId.eq(diaryPostEntity.diaryProfile)
                        .and(diaryUserEntity.userId.eq(user))
                        .and(diaryUserEntity.diaryRole.ne(DiaryUserRoleEnum.SUBSCRIBE)))
                .where(
                        ltLastPostId(lastPostId),
                        condition
                        .and(diaryPostEntity.isDeleted.eq(false)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1)
                .orderBy(diaryPostEntity.createdAt.desc())
                .fetch();

        return new SliceImpl<>(result, pageable, hasNextPage(result, pageable.getPageSize()));
    }

    private boolean hasNextPage(List<DiaryPostEntity> result, int pageSize) {
        if (result.size() > pageSize) {
            result.remove(pageSize);
            return true;
        }
        return false;
    }

    private BooleanExpression ltLastPostId(Long lastPostId) {

        if (lastPostId == null) {
            return null;
        }

        return diaryPostEntity.postId.lt(lastPostId);
    }


}
