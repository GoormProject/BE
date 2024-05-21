package com.ttokttak.jellydiary.feed.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetUserFeedResponseDto {

    private Long postId;
    private Boolean isPublic;
    private Boolean postImgIsMultiple;
    private String postImg;

}
