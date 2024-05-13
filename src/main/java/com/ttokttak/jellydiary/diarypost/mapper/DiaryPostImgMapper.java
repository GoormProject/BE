package com.ttokttak.jellydiary.diarypost.mapper;

import com.ttokttak.jellydiary.diarypost.dto.DiaryPostImgListResponseDto;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostImgEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiaryPostImgMapper {
    DiaryPostImgMapper INSTANCE = Mappers.getMapper(DiaryPostImgMapper.class);

    @Mapping(target = "isDeleted", constant = "false")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DiaryPostImgEntity diaryPostImgRequestToEntity(String imageLink, DiaryPostEntity diaryPost);

    @Mapping(target = "imgId", source = "diaryPostImg.postImgId")
    @Mapping(target = "diaryPostImg", source = "diaryPostImg.imageLink")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DiaryPostImgListResponseDto entityToDiaryPostImgListResponseDto(DiaryPostImgEntity diaryPostImg);
}
