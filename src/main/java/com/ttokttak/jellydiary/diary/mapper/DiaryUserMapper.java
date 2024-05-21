package com.ttokttak.jellydiary.diary.mapper;

import com.ttokttak.jellydiary.diary.dto.DiaryUserResponseDto;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiaryUserMapper {
    DiaryUserMapper INSTANCE = Mappers.getMapper(DiaryUserMapper.class);
    @Mapping(target = "diaryId", source = "diaryId.diaryId")
    @Mapping(target = "userId", source = "userId.userId")
    List<DiaryUserResponseDto> entityToDiaryUserResponseDtoList(List<DiaryUserEntity> entity);

    DiaryUserResponseDto entityToDiaryUserResponseDto(DiaryUserEntity entity);

    default Long mapDiaryProfileToLong(DiaryProfileEntity entity) {
        return entity.getDiaryId();
    }

    default Long mapUserToLong(UserEntity entity) {
        return entity.getUserId();
    }

}
