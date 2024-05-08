package com.ttokttak.jellydiary.util.controller;

import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import com.ttokttak.jellydiary.util.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diary/user/")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @Operation(summary = "사용자 검색", description = "[사용자 검색] api")
    @GetMapping("/{diaryId}/search")
    public ResponseEntity<ResponseDto<?>> getUserSearch(@PathVariable Long diaryId, @RequestParam String searchWord, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(searchService.getUserSearch(diaryId, searchWord, customOAuth2User));
    }


}
