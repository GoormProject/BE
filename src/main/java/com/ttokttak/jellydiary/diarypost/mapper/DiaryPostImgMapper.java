package com.ttokttak.jellydiary.diarypost.mapper;

import com.ttokttak.jellydiary.diarypost.dto.DiaryPostImgListResponseDto;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostImgEntity;

@Mapper(componentModel = "spring")
public interface DiaryPostImgMapper {
    DiaryPostImgMapper INSTANCE = Mappers.getMapper(DiaryPostImgMapper.class);

    @Mapping(target = "isDeleted", constant = "false")
    DiaryPostImgEntity diaryPostImgRequestToEntity(String imageLink, DiaryPostEntity diaryPost);

    @Mapping(target = "imgId", source = "diaryPostImg.postImgId")
    @Mapping(target = "diaryPostImg", source = "diaryPostImg.imageLink")
    DiaryPostImgListResponseDto entityToDiaryPostImgListResponseDto(DiaryPostImgEntity diaryPostImg);
}
