package com.ttokttak.jellydiary.diary.controller;

import com.ttokttak.jellydiary.diary.dto.DiaryProfileRequestDto;
import com.ttokttak.jellydiary.diary.dto.DiaryProfileUpdateRequestDto;
import com.ttokttak.jellydiary.diary.service.DiaryProfileService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryProfileController {

    private final DiaryProfileService diaryProfileService;

    @Operation(summary = "다이어리 생성", description = "[다이어리 생성] api")
    @PostMapping
    public ResponseEntity<ResponseDto<?>> createDiaryProfile(@RequestBody DiaryProfileRequestDto diaryProfileRequestDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(diaryProfileService.createDiaryProfile(diaryProfileRequestDto, customOAuth2User));
    }

    @Operation(summary = "다이어리 정보 수정", description = "[다이어리 정보 수정] api")
    @PatchMapping("/profile/{diaryId}")
    public ResponseEntity<ResponseDto<?>> updateDiaryProfile(@PathVariable("diaryId")Long diaryId, @RequestBody DiaryProfileUpdateRequestDto diaryProfileUpdateRequestDto) {
        return ResponseEntity.ok(diaryProfileService.updateDiaryProfile(diaryId, diaryProfileUpdateRequestDto));
    }

    @Operation(summary = "다이어리 프로필 조회", description = "[다이어리 프로필 조회] api")
    @GetMapping("/profile/{diaryId}")
    public ResponseEntity<ResponseDto<?>> getDiaryProfileInfo(@PathVariable("diaryId")Long diaryId) {
        return ResponseEntity.ok(diaryProfileService.getDiaryProfileInfo(diaryId));
    }

}
