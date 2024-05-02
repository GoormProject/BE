package com.ttokttak.jellydiary.feed.controller;

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
    public ResponseEntity<ResponseDto<?>> getTargetUserFeedInfo(@PathVariable("targetUserId") Long targetUserId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(feedService.getTargetUserFeedInfo(targetUserId, customOAuth2User));
    }

    @Operation(summary = "팔로우 신청", description = "[팔로우 신청] api")
    @PostMapping("/follow/{targetUserId}")
    public ResponseEntity<ResponseDto<?>> createFollowRequest(@PathVariable("targetUserId") Long targetUserId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(feedService.createFollowRequest(targetUserId, customOAuth2User));
    }

    @Operation(summary = "타겟 유저의 팔로워 리스트 조회", description = "[타겟 유저의 팔로워 리스트 조회] api")
    @GetMapping("/followerList/{targetUserId}")
    public ResponseEntity<ResponseDto<?>> getTargetUserFollowerList(@PathVariable("targetUserId") Long targetUserId) {
        return ResponseEntity.ok(feedService.getTargetUserFollowerList(targetUserId));
    }

    @Operation(summary = "타겟 유저의 팔로우 리스트 조회", description = "[타겟 유저의 팔로우 리스트 조회] api")
    @GetMapping("/followList/{targetUserId}")
    public ResponseEntity<ResponseDto<?>> getTargetUserFollowList(@PathVariable("targetUserId") Long targetUserId) {
        return ResponseEntity.ok(feedService.getTargetUserFollowList(targetUserId));
    }

    @Operation(summary = "팔로우 취소", description = "[팔로우 취소] api")
    @DeleteMapping("/follow/{targetUserId}")
    public ResponseEntity<ResponseDto<?>> cancelFollow(@PathVariable("targetUserId") Long targetUserId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(feedService.cancelFollow(targetUserId, customOAuth2User));
    }

}
