package com.ttokttak.jellydiary.chat.service;

import com.ttokttak.jellydiary.chat.dto.ChatMessageListResponseDto;
import com.ttokttak.jellydiary.chat.dto.ChatMessageRequestDto;
import com.ttokttak.jellydiary.chat.dto.ChatMessageResponseDto;
import com.ttokttak.jellydiary.chat.entity.ChatMessageEntity;
import com.ttokttak.jellydiary.chat.entity.ChatRoomEntity;
import com.ttokttak.jellydiary.chat.entity.ChatUserEntity;
import com.ttokttak.jellydiary.chat.mapper.ChatMessageMapper;
import com.ttokttak.jellydiary.chat.repository.ChatMessageRepository;
import com.ttokttak.jellydiary.chat.repository.ChatRoomRepository;
import com.ttokttak.jellydiary.chat.repository.ChatUserRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final UserRepository userRepository;

    private final ChatUserRepository chatUserRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageMapper chatMessageMapper;

    @Override
    @Transactional
    public ChatMessageResponseDto createIncompleteChatMessage(Long chatRoomId, ChatMessageRequestDto chatMessageRequestDto ) {
        UserEntity loginUserEntity = userRepository.findById(chatMessageRequestDto.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        ChatUserEntity chatUserEntity = chatUserRepository.findByChatRoomIdAndUserId(chatRoomEntity, loginUserEntity)
                .orElseThrow(() -> new CustomException(YOU_ARE_NOT_A_CHAT_ROOM_MEMBER));

        ChatMessageEntity chatMessageEntity = ChatMessageEntity.builder()
                .chatMessage(chatMessageRequestDto.getChatMessage())
                .chatRoomId(chatRoomEntity)
                .userId(loginUserEntity)
                .build();
        ChatMessageEntity savedChatMessageEntity = chatMessageRepository.save(chatMessageEntity);

        return chatMessageMapper.entityToChatMessageResponseDto(savedChatMessageEntity);
    }

    @Override
    @Transactional
    public ChatMessageResponseDto createChatMessage(Long chatRoomId, String message, CustomOAuth2User customOAuth2User) {
        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        ChatUserEntity chatUserEntity = chatUserRepository.findByChatRoomIdAndUserId(chatRoomEntity, loginUserEntity)
                .orElseThrow(() -> new CustomException(YOU_ARE_NOT_A_CHAT_ROOM_MEMBER));

        ChatMessageEntity chatMessageEntity = ChatMessageEntity.builder()
                .chatMessage(message)
                .chatRoomId(chatRoomEntity)
                .userId(loginUserEntity)
                .build();
        ChatMessageEntity savedChatMessageEntity = chatMessageRepository.save(chatMessageEntity);

        return chatMessageMapper.entityToChatMessageResponseDto(savedChatMessageEntity);
    }

    @Override
    public ResponseDto<?> getMessagesByChatRoomId(Pageable pageable, Long chatRoomId, CustomOAuth2User customOAuth2User) {
        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        chatUserRepository.findByChatRoomIdAndUserId(chatRoomEntity, loginUserEntity)
                .orElseThrow(() -> new CustomException(YOU_ARE_NOT_A_CHAT_ROOM_MEMBER));

        Slice<ChatMessageEntity> chatMessageEntityPage = chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomEntity, pageable);

        List<ChatMessageResponseDto> chatMessageResponseDtoList = chatMessageEntityPage.getContent().stream()
                .map(chatMessageMapper::entityToChatMessageResponseDto)
                .collect(Collectors.toList());

        ChatMessageListResponseDto chatMessageListResponseDto = ChatMessageListResponseDto.builder()
                .chatMessageList(chatMessageResponseDtoList)
                .hasPrevious(chatMessageEntityPage.hasPrevious())
                .hasNext(chatMessageEntityPage.hasNext())
                .page(chatMessageEntityPage.getNumber())
                .build();

        return ResponseDto.builder()
                .statusCode(SEARCH_CHAT_MESSAGES_SUCCEEDED.getHttpStatus().value())
                .message(SEARCH_CHAT_MESSAGES_SUCCEEDED.getDetail())
                .data(chatMessageListResponseDto)
                .build();
    }
}
