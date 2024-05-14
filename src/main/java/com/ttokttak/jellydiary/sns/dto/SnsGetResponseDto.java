package com.ttokttak.jellydiary.sns.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class SnsGetResponseDto {
    private List<SnsGetListResponseDto> snsList;
    private boolean hasNext;

    @Builder
    public SnsGetResponseDto(List<SnsGetListResponseDto> snsList, boolean hasNext) {
        this.snsList = snsList;
        this.hasNext = hasNext;
    }
}
