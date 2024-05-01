package com.ttokttak.jellydiary.feed.service;

import com.ttokttak.jellydiary.util.dto.ResponseDto;

public interface FeedService {

    ResponseDto<?> getTargetUserFeedInfo(Long targetUserId);

}
