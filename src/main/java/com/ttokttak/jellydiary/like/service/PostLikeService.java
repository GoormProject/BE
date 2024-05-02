package com.ttokttak.jellydiary.like.service;

import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;

public interface PostLikeService {
    ResponseDto<?> createPostLike(Long postId, CustomOAuth2User customOAuth2User);
}
