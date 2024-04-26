package com.ttokttak.jellydiary.diary.controller;

import com.ttokttak.jellydiary.diary.service.DiaryUserService;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diary/userList")
@RequiredArgsConstructor
public class DiaryUserController {

    private final DiaryUserService diaryUserService;

    @Operation(summary = "다이어리 생성자, 참여자 유저 리스트 조회", description = "[다이어리 생성자, 참여자 유저 리스트 조회] api")
    @GetMapping("/{diaryId}")
    public ResponseEntity<ResponseDto<?>> getDiaryParticipantsList(@PathVariable("diaryId")Long diaryId) {
        return ResponseEntity.ok(diaryUserService.getDiaryParticipantsList(diaryId));
    }

}
