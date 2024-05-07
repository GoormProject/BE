package com.ttokttak.jellydiary.feed.service;

import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;

public interface FeedService {

    ResponseDto<?> getTargetUserFeedInfo(Long targetUserId, CustomOAuth2User customOAuth2User);

    ResponseDto<?> createFollowRequest(Long targetUserId, CustomOAuth2User customOAuth2User);

    ResponseDto<?> getTargetUserFollowerList(Long targetUserId, CustomOAuth2User customOAuth2User);

    ResponseDto<?> getTargetUserFollowList(Long targetUserId, CustomOAuth2User customOAuth2User);

    ResponseDto<?> cancelFollow(Long targetUserId, CustomOAuth2User customOAuth2User);

    ResponseDto<?> getTargetUserFeedList(Long targetUserId, CustomOAuth2User customOAuth2User);

}
