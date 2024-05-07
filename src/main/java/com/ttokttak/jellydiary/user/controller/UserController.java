package com.ttokttak.jellydiary.user.controller;

import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.service.UserService;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "유저 프로필 조회", description = "[유저 프로필 조회] api")
    @GetMapping("profile")
    public ResponseEntity<ResponseDto<?>> getUserProflie(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(userService.getUserProflie(customOAuth2User));
    }

}
