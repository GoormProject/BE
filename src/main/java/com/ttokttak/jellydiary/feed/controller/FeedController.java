package com.ttokttak.jellydiary.feed.controller;

import com.ttokttak.jellydiary.diary.dto.DiaryProfileRequestDto;
import com.ttokttak.jellydiary.feed.service.FeedService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    @Operation(summary = "타켓 유저 피드 정보 조회", description = "[타켓 유저 정보 조회] api")
    @GetMapping("/userInfo/{targetUserId}")
    public ResponseEntity<ResponseDto<?>> getTargetUserFeedInfo(@PathVariable("targetUserId") Long targetUserId) {
        return ResponseEntity.ok(feedService.getTargetUserFeedInfo(targetUserId));
    }

}
