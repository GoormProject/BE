package com.ttokttak.jellydiary.diary.controller;

import com.ttokttak.jellydiary.diary.dto.DiaryUserRequestDto;
import com.ttokttak.jellydiary.diary.dto.DiaryUserUpdateRoleRequestDto;
import com.ttokttak.jellydiary.diary.service.DiaryUserService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diary/user")
@RequiredArgsConstructor
public class DiaryUserController {

    private final DiaryUserService diaryUserService;

    @Operation(summary = "다이어리 생성자, 참여자 유저 리스트 조회", description = "[다이어리 생성자, 참여자 유저 리스트 조회] api")
    @GetMapping("/list/{diaryId}")
    public ResponseEntity<ResponseDto<?>> getDiaryParticipantsList(@PathVariable("diaryId")Long diaryId) {
        return ResponseEntity.ok(diaryUserService.getDiaryParticipantsList(diaryId));
    }

    @Operation(summary = "다이어리 유저 생성", description = "[다이어리 유저 생성] api")
    @PostMapping()
    public ResponseEntity<ResponseDto<?>> createDiaryUser(@RequestBody DiaryUserRequestDto diaryUserRequestDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(diaryUserService.createDiaryUser(diaryUserRequestDto, customOAuth2User));
    }

    @Operation(summary = "다이어리 유저들 Role 수정", description = "[다이어리 유저들 Role 수정] api")
    @PatchMapping("/list/{diaryId}")
    public ResponseEntity<ResponseDto<?>> updateDiaryParticipantsRolesList(@PathVariable("diaryId")Long diaryId, @RequestBody List<DiaryUserUpdateRoleRequestDto> updateRequestDtoList, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(diaryUserService.updateDiaryParticipantsRolesList(diaryId, updateRequestDtoList, customOAuth2User));
    }


}
