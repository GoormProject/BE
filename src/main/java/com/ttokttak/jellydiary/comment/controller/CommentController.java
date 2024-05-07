package com.ttokttak.jellydiary.comment.controller;

import com.ttokttak.jellydiary.comment.dto.CommentCreateRequestDto;
import com.ttokttak.jellydiary.comment.service.CommentService;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "[댓글 작성] api")
    @PostMapping("/{postId}")
    public ResponseEntity<ResponseDto<?>> createComment(@PathVariable Long postId, @RequestBody CommentCreateRequestDto commentCreateRequestDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(commentService.createComment(postId, commentCreateRequestDto, customOAuth2User));
    }

    @Operation(summary = "댓글에 답글 작성", description = "[댓글에 답글 작성] api")
    @PostMapping("/{postId}/{commentId}")
    public ResponseEntity<ResponseDto<?>> createReplyComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentCreateRequestDto commentCreateRequestDto, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return ResponseEntity.ok(commentService.createReplyComment(postId, commentId, commentCreateRequestDto, customOAuth2User));
    }


}
