package com.ttokttak.jellydiary.diary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import com.ttokttak.jellydiary.diary.dto.DiaryProfileDto;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;

@Mapper(componentModel = "spring")
public interface DiaryProfileMapper {
    DiaryProfileMapper INSTANCE = Mappers.getMapper(DiaryProfileMapper.class);

    DiaryProfileDto entityToDto(DiaryProfileEntity entity);
    DiaryProfileEntity dtoToEntity(DiaryProfileDto dto);

    void updateEntityFromDto(DiaryProfileDto dto, @MappingTarget DiaryProfileEntity entity);
}
