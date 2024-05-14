package com.ttokttak.jellydiary.chat.mapper;

import com.ttokttak.jellydiary.chat.dto.ChatMessageResponseDto;
import com.ttokttak.jellydiary.chat.entity.ChatMessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    ChatMessageMapper INSTANCE = Mappers.getMapper(ChatMessageMapper.class);
    @Mapping(target = "chatRoomId", source = "chatRoomId.chatRoomId")
    @Mapping(target = "userId", source = "userId.userId")
    @Mapping(target = "userName", source = "userId.userName")
    @Mapping(target = "userProfileImg", source = "userId.profileImg")
    ChatMessageResponseDto entityToChatMessageResponseDto(ChatMessageEntity entity);

}
