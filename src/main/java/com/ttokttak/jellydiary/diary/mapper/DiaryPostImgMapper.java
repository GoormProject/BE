package com.ttokttak.jellydiary.diary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.ttokttak.jellydiary.diary.dto.DiaryPostImgDto;
import com.ttokttak.jellydiary.diary.entity.DiaryPostImgEntity;

@Mapper
public interface DiaryPostImgMapper {
    DiaryPostImgMapper INSTANCE = Mappers.getMapper(DiaryPostImgMapper.class);

    DiaryPostImgDto entityToDto(DiaryPostImgEntity entity);
    DiaryPostImgEntity dtoToEntity(DiaryPostImgDto dto);
}
