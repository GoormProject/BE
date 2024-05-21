package com.ttokttak.jellydiary.chat.service;

import com.ttokttak.jellydiary.chat.dto.ChatRoomRequestDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;

public interface ChatRoomService {

    ResponseDto<?> getOrCreateChatRoomId(ChatRoomRequestDto chatRoomRequestDto, CustomOAuth2User customOAuth2User);

    String getDestinationFromChatRoomType(Long chatRoomId);

    ResponseDto<?> getMyChatRoomList(CustomOAuth2User customOAuth2User);

}
