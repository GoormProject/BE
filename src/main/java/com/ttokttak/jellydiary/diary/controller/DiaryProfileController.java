package com.ttokttak.jellydiary.diary.controller;

import com.ttokttak.jellydiary.diary.dto.DiaryProfileRequestDto;
import com.ttokttak.jellydiary.diary.dto.DiaryProfileUpdateRequestDto;
import com.ttokttak.jellydiary.diary.service.DiaryProfileService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<ResponseDto<?>> updateDiaryProfile(@PathVariable("diaryId")Long diaryId, @RequestBody DiaryProfileUpdateRequestDto diaryProfileUpdateRequestDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(diaryProfileService.updateDiaryProfile(diaryId, diaryProfileUpdateRequestDto, customOAuth2User));
    }

    @Operation(summary = "다이어리 프로필 조회", description = "[다이어리 프로필 조회] api")
    @GetMapping("/profile/{diaryId}")
    public ResponseEntity<ResponseDto<?>> getDiaryProfileInfo(@PathVariable("diaryId")Long diaryId) {
        return ResponseEntity.ok(diaryProfileService.getDiaryProfileInfo(diaryId));
    }

    @Operation(summary = "내가 구독 또는 참여 중인 다이어리 리스트 조회", description = "[내가 구독 또는 참여 중인 다이어리 리스트 조회] api")
    @GetMapping("/mydiaryList")
    public ResponseEntity<ResponseDto<?>> getMySubscribedOrParticipatingDiariesList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(diaryProfileService.getMySubscribedOrParticipatingDiariesList(customOAuth2User));
    }

    @Operation(summary = "다이어리 삭제", description = "[다이어리 삭제] api")
    @DeleteMapping("/profile/{diaryId}")
    public ResponseEntity<ResponseDto<?>> deleteDiaryProfile(@PathVariable("diaryId")Long diaryId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(diaryProfileService.deleteDiaryProfile(diaryId, customOAuth2User));
    }

}
