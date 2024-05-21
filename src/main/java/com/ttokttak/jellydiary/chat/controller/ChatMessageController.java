package com.ttokttak.jellydiary.chat.controller;

import com.ttokttak.jellydiary.chat.dto.ChatMessageRequestDto;
import com.ttokttak.jellydiary.chat.dto.ChatMessageResponseDto;
import com.ttokttak.jellydiary.chat.service.ChatMessageService;
import com.ttokttak.jellydiary.chat.service.ChatRoomService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate template;

    private final ChatMessageService chatMessageService;

    private final ChatRoomService chatRoomService;

    @MessageMapping("/{chatRoomId}")
    public void message(@DestinationVariable Long chatRoomId,@Payload ChatMessageRequestDto chatMessageRequestDto){
        ChatMessageResponseDto chatMessage = chatMessageService.createIncompleteChatMessage(chatRoomId, chatMessageRequestDto);
        String destination = chatRoomService.getDestinationFromChatRoomType(chatRoomId);

        template.convertAndSend(destination + chatRoomId, chatMessage);
    }

    /* 수정 예정
    @MessageMapping("/{chatRoomId}")
    public void message(@DestinationVariable Long chatRoomId, @Payload String message, @AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        ChatMessageResponseDto chatMessage = chatMessageService.createChatMessage(chatRoomId, message, customOAuth2User);
        String destination = chatRoomService.getDestinationFromChatRoomType(chatRoomId);

        template.convertAndSend(destination + chatRoomId, chatMessage);
    }
     */

}
