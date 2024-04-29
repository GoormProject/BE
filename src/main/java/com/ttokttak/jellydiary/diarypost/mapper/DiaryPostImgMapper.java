package com.ttokttak.jellydiary.diarypost.mapper;

import com.ttokttak.jellydiary.diarypost.dto.DiaryPostImgListResponseDto;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.ttokttak.jellydiary.diarypost.dto.DiaryPostImgDto;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostImgEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiaryPostImgMapper {
    DiaryPostImgMapper INSTANCE = Mappers.getMapper(DiaryPostImgMapper.class);

    @Mapping(target = "imageLink", expression = "java(postImg.getOriginalFilename())")
    DiaryPostImgEntity diaryPostImgRequestToEntity(MultipartFile postImg, DiaryPostEntity diaryPost);

    @Mapping(target = "diaryPostImg", source = "diaryPostImg.imageLink")
    DiaryPostImgListResponseDto entityToDiaryPostImgListResponseDto(DiaryPostImgEntity diaryPostImg);
}
