package com.ttokttak.jellydiary.chat.service;

import com.ttokttak.jellydiary.chat.dto.ChatMessageRequestDto;
import com.ttokttak.jellydiary.chat.dto.ChatMessageResponseDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import org.springframework.data.domain.Pageable;

public interface ChatMessageService {

    ChatMessageResponseDto createIncompleteChatMessage(Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto);
    ChatMessageResponseDto createChatMessage(Long chatRoomId, String message, CustomOAuth2User customOAuth2User);
    ResponseDto<?> getMessagesByChatRoomId(Pageable pageable, Long chatRoomId, CustomOAuth2User customOAuth2User);

}
