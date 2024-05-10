package com.ttokttak.jellydiary.sns.service;

import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.like.repository.PostLikeRepository;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SnsServiceImpl implements SnsService {
    private final UserRepository userRepository;
    private final DiaryProfileRepository diaryProfileRepository;
    private final DiaryPostRepository diaryPostRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    public ResponseDto<?> getSnsList(CustomOAuth2User customOAuth2User) {
        UserEntity userEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //1. 다이어리 포스트 전체 조회
        //2. for문 돌며 포스트마다 확인하여 isPublic값이 false인 경우
        //      - diaryId를 확인하여 diaryId와 userEntity를 가지고 diaryUser 테이블을 조회하여 권한이 있는 다이어리 인지 확인
        //          -- diaryUser테이블에 존재한다면 SUBSCRIBE가 아닐 경우 리스트에 추가
        //          -- diaryUser테이블에 존재하지 않는다면 건너뜀
        //3. for문 돌며 포스트마다 확인하여 isPublic값이 true인 경우
        //      - 리스트에 추가



        return null;
    }
}
