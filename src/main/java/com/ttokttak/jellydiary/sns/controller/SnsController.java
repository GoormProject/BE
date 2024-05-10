package com.ttokttak.jellydiary.sns.controller;

import com.ttokttak.jellydiary.sns.service.SnsService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jellyDiary")
@RequiredArgsConstructor
public class SnsController {
    private final SnsService snsService;

    @Operation(summary = "SNS 게시물 리스트 조회", description = "[SNS 게시물 리스트 조회] api - 1페이지당 10개씩")
    @GetMapping("/")
    public ResponseEntity<ResponseDto<?>> getSnsList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(snsService.getSnsList(customOAuth2User));
    }



}
