package com.ttokttak.jellydiary.diary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.ttokttak.jellydiary.diary.dto.DiaryPostDto;
import com.ttokttak.jellydiary.diary.entity.DiaryPostEntity;

@Mapper(componentModel = "spring")
public interface DiaryPostMapper {
    DiaryPostMapper INSTANCE = Mappers.getMapper(DiaryPostMapper.class);

    DiaryPostDto entityToDto(DiaryPostEntity entity);
    DiaryPostEntity dtoToEntity(DiaryPostDto dto);
}
