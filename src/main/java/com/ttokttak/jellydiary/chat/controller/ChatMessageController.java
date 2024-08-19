package com.ttokttak.jellydiary.chat.controller;

import com.ttokttak.jellydiary.chat.dto.ChatMessageRequestDto;
import com.ttokttak.jellydiary.chat.dto.ChatMessageResponseDto;
import com.ttokttak.jellydiary.chat.service.ChatMessageService;
import com.ttokttak.jellydiary.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    @Value("${rabbitmq.exchange.name}")
    private String EXCHANGE_NAME;

    //private final SimpMessagingTemplate template;
    private final RabbitTemplate rabbitTemplate;

    private final ChatMessageService chatMessageService;

    private final ChatRoomService chatRoomService;

    @MessageMapping("/{chatRoomId}")
    public void message(@DestinationVariable Long chatRoomId,@Payload ChatMessageRequestDto chatMessageRequestDto){
        ChatMessageResponseDto chatMessage = chatMessageService.createIncompleteChatMessage(chatRoomId, chatMessageRequestDto);
        String destination = chatRoomService.getDestinationFromChatRoomType(chatRoomId);
        //template.convertAndSend(destination + chatRoomId, chatMessage);

        String routingKey = destination + chatRoomId;
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routingKey, chatMessage);
    }

}
