package com.ttokttak.jellydiary.diarypost.mapper;

import com.ttokttak.jellydiary.diarypost.entity.DiaryPostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.ttokttak.jellydiary.diarypost.dto.DiaryPostDto;

@Mapper(componentModel = "spring")
public interface DiaryPostMapper {
    DiaryPostMapper INSTANCE = Mappers.getMapper(DiaryPostMapper.class);

    DiaryPostDto entityToDto(DiaryPostEntity entity);
    DiaryPostEntity dtoToEntity(DiaryPostDto dto);
}
