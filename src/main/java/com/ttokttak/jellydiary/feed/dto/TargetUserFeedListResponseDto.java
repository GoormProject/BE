package com.ttokttak.jellydiary.feed.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TargetUserFeedListResponseDto {

    private int count;

    private List<TargetUserFeedResponseDto> feeds;

}
