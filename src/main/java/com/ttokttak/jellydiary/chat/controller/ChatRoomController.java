package com.ttokttak.jellydiary.chat.controller;

import com.ttokttak.jellydiary.chat.dto.ChatRoomRequestDto;
import com.ttokttak.jellydiary.chat.service.ChatMessageService;
import com.ttokttak.jellydiary.chat.service.ChatRoomService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    private final ChatMessageService chatMessageService;

    @Operation(summary = "채팅방 아이디 조회 및 생성", description = "[채팅방 아이디 조회 및 생성] api")
    @PostMapping("/room")
    public ResponseEntity<ResponseDto<?>> getOrCreateChatRoomId(@RequestBody ChatRoomRequestDto chatRoomRequestDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(chatRoomService.getOrCreateChatRoomId(chatRoomRequestDto, customOAuth2User));
    }

    @Operation(summary = "나의 채팅 리스트 조회", description = "[나의 채팅 리스트 조회] api")
    @GetMapping("/roomList")
    public ResponseEntity<ResponseDto<?>> getMyChatRoomList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(chatRoomService.getMyChatRoomList(customOAuth2User));
    }

    @Operation(summary = "채팅 내역 상세 조회", description = "[채팅 내역 상세 조회] api")
    @GetMapping("/messages/{chatRoomId}")
    public ResponseEntity<ResponseDto<?>> getMessagesByChatRoomId(@PathVariable("chatRoomId")Long chatRoomId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(chatMessageService.getMessagesByChatRoomId(chatRoomId, customOAuth2User));
    }

}
