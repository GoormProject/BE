package com.ttokttak.jellydiary.sns.service;

import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import org.springframework.data.domain.Pageable;

public interface SnsService {
    ResponseDto<?> getSnsList(Pageable pageable, Long lastPostId, CustomOAuth2User customOAuth2User);
}
