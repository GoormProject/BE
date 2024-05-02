package com.ttokttak.jellydiary.like.controller;

import com.ttokttak.jellydiary.like.service.PostLikeService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post/like")
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;

    @Operation(summary = "게시물 좋아요 등록", description = "[게시물 좋아요 등록] api")
    @PostMapping("/{postId}")
    public ResponseEntity<ResponseDto<?>> createPostLike(@PathVariable Long postId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(postLikeService.createPostLike(postId, customOAuth2User));
    }

    @Operation(summary = "게시물 조회", description = "[게시물 좋아요 조회] api")
    @GetMapping("/{postId}")
    public ResponseEntity<ResponseDto<?>> getPostLike(@PathVariable Long postId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(postLikeService.getPostLike(postId, customOAuth2User));
    }

}
