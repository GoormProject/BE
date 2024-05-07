package com.ttokttak.jellydiary.user.mapper;

import com.ttokttak.jellydiary.notification.entity.NotificationSettingEntity;
import com.ttokttak.jellydiary.user.dto.UserProfileDto;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "userEntity.userId", target = "userId")
    UserProfileDto entitiytoUserProfileDto(UserEntity userEntity, NotificationSettingEntity notificationSettingEntity);
}
