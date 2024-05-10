package com.ttokttak.jellydiary.chat.controller;

import com.ttokttak.jellydiary.chat.dto.ChatRoomRequestDto;
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
@RequestMapping("/api/chat/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 아이디 조회 및 생성", description = "[채팅방 아이디 조회 및 생성] api")
    @PostMapping
    public ResponseEntity<ResponseDto<?>> getOrCreateChatRoomId(@RequestBody ChatRoomRequestDto chatRoomRequestDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(chatRoomService.getOrCreateChatRoomId(chatRoomRequestDto, customOAuth2User));
    }

}
