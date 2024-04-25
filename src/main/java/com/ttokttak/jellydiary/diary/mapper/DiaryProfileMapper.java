package com.ttokttak.jellydiary.diary.mapper;

import com.ttokttak.jellydiary.diary.dto.DiaryProfileRequestDto;
import com.ttokttak.jellydiary.diary.dto.DiaryProfileResponseDto;
import com.ttokttak.jellydiary.diary.dto.DiaryProfileUpdateResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;

@Mapper(componentModel = "spring")
public interface DiaryProfileMapper {
    DiaryProfileMapper INSTANCE = Mappers.getMapper(DiaryProfileMapper.class);

    @Mapping(target = "isDiaryDeleted", constant = "false")
    DiaryProfileEntity diaryProfileRequestDtoToEntity(DiaryProfileRequestDto dto);

    @Mapping(target = "chatRoomId", source = "chatRoomId.chatRoomId")
    DiaryProfileResponseDto entityToDiaryProfileResponseDto(DiaryProfileEntity entity);

    DiaryProfileUpdateResponseDto entityToDiaryProfileUpdateResponseDto(DiaryProfileEntity entity);

}
