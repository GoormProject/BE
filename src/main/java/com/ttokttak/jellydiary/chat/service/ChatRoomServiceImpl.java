package com.ttokttak.jellydiary.chat.service;

import com.ttokttak.jellydiary.chat.dto.ChatRoomRequestDto;
import com.ttokttak.jellydiary.chat.dto.ChatRoomResponseDto;
import com.ttokttak.jellydiary.chat.dto.ChatRoomTypeEnum;
import com.ttokttak.jellydiary.chat.entity.ChatMessageEntity;
import com.ttokttak.jellydiary.chat.entity.ChatRoomEntity;
import com.ttokttak.jellydiary.chat.entity.ChatUserEntity;
import com.ttokttak.jellydiary.chat.repository.ChatMessageRepository;
import com.ttokttak.jellydiary.chat.repository.ChatRoomRepository;
import com.ttokttak.jellydiary.chat.repository.ChatUserRepository;
import com.ttokttak.jellydiary.diary.entity.DiaryProfileEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserEntity;
import com.ttokttak.jellydiary.diary.entity.DiaryUserRoleEnum;
import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diary.repository.DiaryUserRepository;
import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomServiceImpl implements ChatRoomService {

    private final DiaryProfileRepository diaryProfileRepository;

    private final DiaryUserRepository diaryUserRepository;

    private final UserRepository userRepository;

    private final ChatUserRepository chatUserRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public ResponseDto<?> getOrCreateChatRoomId(ChatRoomRequestDto chatRoomRequestDto, CustomOAuth2User customOAuth2User) {
        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Long chatRoomId = -1L;

        ChatRoomTypeEnum chatRoomType = ChatRoomTypeEnum.valueOf(chatRoomRequestDto.getChatRoomType().toUpperCase());
        if(chatRoomType == ChatRoomTypeEnum.GROUP) {
            DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(chatRoomRequestDto.getDiaryId())
                    .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

            Optional<DiaryUserEntity> loginUserInDiaryOpt = diaryUserRepository.findByDiaryIdAndUserId(diaryProfileEntity, loginUserEntity);
            if (loginUserInDiaryOpt.isEmpty() || loginUserInDiaryOpt.get().getDiaryRole().equals(DiaryUserRoleEnum.SUBSCRIBE)) {
                throw new CustomException(YOU_ARE_NOT_A_CHAT_ROOM_MEMBER);
            }

            chatRoomId = diaryProfileEntity.getChatRoomId().getChatRoomId();
        }
        else if(chatRoomType == ChatRoomTypeEnum.PRIVATE) {
            UserEntity targetUserEntity = userRepository.findById(chatRoomRequestDto.getUserId())
                    .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

            if(loginUserEntity.getUserId().equals(targetUserEntity.getUserId())){
                throw new CustomException(CANNOT_CHAT_WITH_SELF);
            }

            List<ChatRoomEntity> commonChatRooms = chatUserRepository.findCommonChatRooms(loginUserEntity, targetUserEntity);
            for(ChatRoomEntity cm : commonChatRooms){
                if(cm.getChatRoomName().startsWith("private_")){
                    chatRoomId = cm.getChatRoomId();
                    break;
                }
            }

            if(chatRoomId == -1L){ //채팅방 생성
                String chatRoomName = "private_" + loginUserEntity.getUserId() + "_" + targetUserEntity.getUserId();
                ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                        .chatRoomName(chatRoomName)
                        .build();
                ChatRoomEntity savedChatRoomEntity = chatRoomRepository.save(chatRoomEntity);

                List<ChatUserEntity> chatUserEntities = Arrays.asList(
                        ChatUserEntity.builder().chatRoomId(savedChatRoomEntity).userId(loginUserEntity).build(),
                        ChatUserEntity.builder().chatRoomId(savedChatRoomEntity).userId(targetUserEntity).build()
                );
                chatUserRepository.saveAll(chatUserEntities);

                chatRoomId = savedChatRoomEntity.getChatRoomId();
            }
        }

        return ResponseDto.builder()
                .statusCode(SEARCH_CHAT_ROOM_ID_SUCCESS.getHttpStatus().value())
                .message(SEARCH_CHAT_ROOM_ID_SUCCESS.getDetail())
                .data(chatRoomId)
                .build();
    }

    @Override
    public String getDestinationFromChatRoomType(Long chatRoomId) {
        ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHAT_ROOM_NOT_FOUND));

        if(chatRoomEntity.getChatRoomName().startsWith("group_")){
            return "/topic/group/";
        }else{
            return "/queue/private/";
        }
    }

    @Override
    @Transactional
    public ResponseDto<?> getMyChatRoomList(CustomOAuth2User customOAuth2User) {
        UserEntity loginUserEntity = userRepository.findById(customOAuth2User.getUserId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<ChatRoomResponseDto> chatRoomResponseDtoList = new ArrayList<>();

        List<ChatRoomEntity> chatRoomEntities = chatUserRepository.findChatRoomsByUserId(loginUserEntity);
        for(ChatRoomEntity chatRoomEntity : chatRoomEntities){
            ChatRoomResponseDto.ChatRoomResponseDtoBuilder chatRoomResponseDtoBuilder = ChatRoomResponseDto.builder();

            Pageable pageable = PageRequest.of(0, 1, Sort.by("createdAt").descending());
            Page<ChatMessageEntity> chatMessageEntityPage = chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomEntity, pageable);
            List<ChatMessageEntity> chatMessageEntities = chatMessageEntityPage.getContent();
            String messagePreview = "";
            LocalDateTime createdAt = null;
            if (!chatMessageEntities.isEmpty()) {
                messagePreview = chatMessageEntities.get(0).getChatMessage();
                createdAt = chatMessageEntities.get(0).getCreatedAt();
            } else {
                messagePreview = "새로운 채팅방이 생성되었습니다. 첫 번째 메시지를 보내보세요.";
            }

            chatRoomResponseDtoBuilder.chatRoomId(chatRoomEntity.getChatRoomId())
                    .chatMessagePreview(messagePreview)
                    .createdAt(createdAt);

            String[] splitRoomName = chatRoomEntity.getChatRoomName().split("_");
            if(splitRoomName[0].equals("group")){
                DiaryProfileEntity diaryProfileEntity = diaryProfileRepository.findById(Long.parseLong(splitRoomName[1]))
                        .orElseThrow(() -> new CustomException(DIARY_NOT_FOUND));

                chatRoomResponseDtoBuilder.chatRoomName(diaryProfileEntity.getDiaryName())
                        .chatRoomProfileImg(diaryProfileEntity.getDiaryProfileImage());
            }else{
                Long recipientId = (Long.parseLong(splitRoomName[1]) == loginUserEntity.getUserId()) ? Long.parseLong(splitRoomName[2]) : Long.parseLong(splitRoomName[1]);
                UserEntity recipientUserEntity = userRepository.findById(recipientId)
                        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

                chatRoomResponseDtoBuilder.chatRoomName(recipientUserEntity.getUserName())
                        .chatRoomProfileImg(recipientUserEntity.getProfileImg());
            }

            ChatRoomResponseDto chatRoomResponseDto = chatRoomResponseDtoBuilder.build();
            chatRoomResponseDtoList.add(chatRoomResponseDto);
        }
        return ResponseDto.builder()
                .statusCode(SEARCH_MY_CHAT_LIST_SUCCESS.getHttpStatus().value())
                .message(SEARCH_MY_CHAT_LIST_SUCCESS.getDetail())
                .data(chatRoomResponseDtoList)
                .build();
    }

}
