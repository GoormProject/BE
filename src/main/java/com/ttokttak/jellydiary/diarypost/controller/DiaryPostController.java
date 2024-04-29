package com.ttokttak.jellydiary.diarypost.controller;

import com.ttokttak.jellydiary.diarypost.dto.DiaryPostCreateRequestDto;
import com.ttokttak.jellydiary.diarypost.service.DiaryPostService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class DiaryPostController {
    private final DiaryPostService diaryPostService;

    @Operation(summary = "게시물 생성", description = "[게시물 생성] api")
    @PostMapping("/{diaryId}")
    public ResponseEntity<ResponseDto<?>> createDiaryPost(@PathVariable Long diaryId, @RequestPart DiaryPostCreateRequestDto diaryPostCreateRequestDto, @RequestPart(value = "postImgs", required = false) List<MultipartFile> postImgs, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(diaryPostService.createDiaryPost(diaryId, diaryPostCreateRequestDto, postImgs, customOAuth2User));
    }

}
