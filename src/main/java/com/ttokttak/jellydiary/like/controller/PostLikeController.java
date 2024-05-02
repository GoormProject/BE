package com.ttokttak.jellydiary.like.controller;

import com.ttokttak.jellydiary.like.service.PostLikeService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post/like")
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService postLikeService;

    @Operation(summary = "게시물 좋아요", description = "[게시물 좋아요] api")
    @PostMapping("/{postId}")
    public ResponseEntity<ResponseDto<?>> createPostLike(@PathVariable Long postId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(postLikeService.createPostLike(postId, customOAuth2User));
    }

}
