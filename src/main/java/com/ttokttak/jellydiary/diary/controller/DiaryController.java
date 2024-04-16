package com.ttokttak.jellydiary.diary.controller;

import com.ttokttak.jellydiary.diary.dto.DiaryPostDto;
import com.ttokttak.jellydiary.diary.dto.DiaryProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ttokttak.jellydiary.diary.service.DiaryService;

@RestController
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;
    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    // 다이어리 생성
    @PostMapping
    public ResponseEntity<?> createDiary(@RequestBody DiaryProfileDto diaryProfileDto) {
        DiaryProfileDto createdDiary = diaryService.createDiary(diaryProfileDto);
        return ResponseEntity.status(201).body(createdDiary);
    }

    // 다이어리 정보 수정
    @PatchMapping("/profile")
    public ResponseEntity<DiaryProfileDto> updateDiaryProfile(@RequestBody DiaryProfileDto diaryProfileDto) {
        DiaryProfileDto updatedDiary = diaryService.updateDiaryProfile(diaryProfileDto);
        return ResponseEntity.ok(updatedDiary);
    }

    // 다이어리 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<DiaryProfileDto> getDiaryProfile(@RequestParam Long diaryId) {
        DiaryProfileDto diaryProfile = diaryService.getDiaryProfile(diaryId);
        return ResponseEntity.ok(diaryProfile);
    }

    // 내가 쓴 글들을 전체 다이어리 리스트로 조회
    @GetMapping("/mydiarylist")
    public String getMyDiaries() {
        // 로직 구현
        return "List of my diaries";
    }

    // 다이어리 포스트 리스트 조회
    @GetMapping("/postlist")
    public String getDiaryPosts() {
        // 로직 구현
        return "Diary post list";
    }

    // 다이어리 사용자, 작성자 포스트 리스트 조회
    @GetMapping("/userlist")
    public String getUserPosts() {
        // 로직 구현
        return "User post list";
    }
}
