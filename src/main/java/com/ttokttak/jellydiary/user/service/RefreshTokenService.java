package com.ttokttak.jellydiary.user.service;

import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.jwt.JWTUtil;
import com.ttokttak.jellydiary.user.entity.RefreshTokenEntity;
import com.ttokttak.jellydiary.user.repository.RefreshTokenRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.TOKEN_REISSUED_SUCCESS;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        // get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
                break;
            }
        }

        if (refresh == null) {
            throw new CustomException(REFRESH_TOKEN_NULL);
        }

        // 토큰이 만료되었는지 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomException(REFRESH_TOKEN_EXPIRED);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtUtil.getUserId(refresh);
        String userName = jwtUtil.getUserName(refresh);
        String authority = jwtUtil.getAuthority(refresh);

        String newAccess = jwtUtil.createAccessJwt("access", userId, userName, authority);
        String newRefresh = jwtUtil.createRefreshJwt("refresh", userId, userName, authority);

        // Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshTokenRepository.deleteByRefreshToken(refresh);
        addRefreshTokenEntity(userName, newRefresh);

        response.setHeader("Authorization", newAccess);
        ResponseCookie refreshTokenCookie = jwtUtil.createCookie("refresh", newRefresh);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
//        response.addCookie(refreshTokenCookie);
//        response.addCookie(jwtUtil.createCookie("refresh", newRefresh));

        System.out.println("Authorization Header: " + response.getHeader("Authorization"));
        System.out.println("RefreshToken Cookie: " + refreshTokenCookie.getName() + "=" + refreshTokenCookie.getValue());

        return ResponseDto.builder()
                .statusCode(TOKEN_REISSUED_SUCCESS.getHttpStatus().value())
                .message(TOKEN_REISSUED_SUCCESS.getDetail())
                .build();
    }

    public void addRefreshTokenEntity(String refreshToken, String username) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .refreshToken(refreshToken)
                .username(username)
                .expiration(jwtUtil.getRefreshTokenExpireTime() / 1000) // Convert milliseconds to seconds
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }
}
