package com.ttokttak.jellydiary.feed.service;

import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;

public interface FeedService {

    ResponseDto<?> getTargetUserFeedInfo(Long targetUserId);

    ResponseDto<?> createFollowRequest(Long targetUserId, CustomOAuth2User customOAuth2User);

    ResponseDto<?> getTargetUserFollowerList(Long targetUserId);

    ResponseDto<?> getTargetUserFollowList(Long targetUserId);

    ResponseDto<?> cancelFollow(Long targetUserId, CustomOAuth2User customOAuth2User);

}
