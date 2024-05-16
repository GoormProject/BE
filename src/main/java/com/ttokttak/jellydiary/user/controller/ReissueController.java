package com.ttokttak.jellydiary.user.controller;

import com.ttokttak.jellydiary.user.service.RefreshTokenService;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReissueController {
    private final RefreshTokenService refreshTokenService;

    @Operation(summary = "엑세스 토큰 재발급", description = "[엑세스 토큰 재발급] api")
    @PostMapping("/api/reissue")
    public ResponseEntity<ResponseDto<?>> reissue(HttpServletRequest request, HttpServletResponse response) {
        ResponseDto<?> result = refreshTokenService.reissue(request, response);
        return ResponseEntity.status(result.getStatusCode()).body(result);
    }
}
