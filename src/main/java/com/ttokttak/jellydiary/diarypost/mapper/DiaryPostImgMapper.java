package com.ttokttak.jellydiary.diarypost.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.ttokttak.jellydiary.diarypost.dto.DiaryPostImgDto;
import com.ttokttak.jellydiary.diarypost.entity.DiaryPostImgEntity;

@Mapper(componentModel = "spring")
public interface DiaryPostImgMapper {
    DiaryPostImgMapper INSTANCE = Mappers.getMapper(DiaryPostImgMapper.class);

    DiaryPostImgDto entityToDto(DiaryPostImgEntity entity);
    DiaryPostImgEntity dtoToEntity(DiaryPostImgDto dto);
}
