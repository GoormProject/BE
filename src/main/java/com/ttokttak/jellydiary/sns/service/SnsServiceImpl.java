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
import org.springframework.data.domain.Pageable;
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
    public ResponseDto<?> getSnsList(Pageable pageable, Long lastPostId, CustomOAuth2User customOAuth2User) {
        return null;
    }
}
