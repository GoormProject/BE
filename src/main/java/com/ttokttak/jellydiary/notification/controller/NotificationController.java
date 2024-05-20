package com.ttokttak.jellydiary.notification.controller;

import com.ttokttak.jellydiary.notification.service.NotificationService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "SSE 세션 연결", description = "[SSE 세션 연결] api")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@Parameter(hidden = true) @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return ResponseEntity.ok(notificationService.subscribe(customOAuth2User, lastEventId));
    }

    @Operation(summary = "알림 리스트 조회", description = "[알림 리스트 조회] api")
    @GetMapping("/notification")
    public ResponseEntity<ResponseDto<?>> getListNotification(@Parameter(hidden = true) @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(notificationService.getListNotification(customOAuth2User));
    }

    @Operation(summary = "알림 전체 삭제", description = "[알림 전체 삭제] api")
    @DeleteMapping("/notification/{userId}")
    public ResponseEntity<ResponseDto<?>> deleteNotification(@PathVariable Long userId, @Parameter(hidden = true) @AuthenticationPrincipal CustomOAuth2User customOAuth2User){
        return ResponseEntity.ok(notificationService.deleteNotification(userId, customOAuth2User));
    }
}
