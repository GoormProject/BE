package com.ttokttak.jellydiary.util.mapper;

import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.util.dto.SearchUserListResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SearchMapper {
    SearchMapper INSTANCE = Mappers.getMapper(SearchMapper.class);

    SearchUserListResponseDto entityToUserListResponseDto(UserEntity user, Boolean isInvited);
}
