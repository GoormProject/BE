package com.ttokttak.jellydiary.user.service;

import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.jwt.JWTUtil;
import com.ttokttak.jellydiary.user.dto.AccessTokenDto;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.TOKEN_REISSUED_SUCCESS;
import static com.ttokttak.jellydiary.exception.message.SuccessMsg.UPDATE_USER_PROFILE_SUCCESS;


@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        // Get refresh token from cookies
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
                break;
            }
        }

        // Refresh 토큰이 있는지 확인
        if (refresh == null) {
            throw new CustomException(REFRESH_TOKEN_NULL);
        }

        Long userId = jwtUtil.getUserId(refresh);
        String userName = jwtUtil.getUserName(refresh);
        String authority = jwtUtil.getAuthority(refresh);
        String category = jwtUtil.getCategory(refresh);
        String refreshTokenEntityId = userId + ":" + refresh;

        // Refresh 토큰이 만료되었는지 확인(Validate)
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomException(REFRESH_TOKEN_EXPIRED);
        }

        // Refresh 토큰의 category가 refresh인지 확인 (발급시 페이로드에 명시) (Verify)
        if (!category.equals("refresh")) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }

        // 들어온 Refresh 토큰이 가장 최근에 발급된 토큰인지 확인
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findById(refreshTokenEntityId)
                .orElseThrow(() -> new CustomException(INVALID_REFRESH_TOKEN));

        // 쿠키에서 추출한 Refresh 토큰값이 RefreshTokenEntity의 refreshToken값과 같은지 확인
        if (!refreshTokenEntity.getRefreshToken().equals(refresh)) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }

        String newAccess = jwtUtil.createAccessJwt("access", userId, userName, authority);
        String newRefresh = jwtUtil.createRefreshJwt("refresh", userId, userName, authority);

        // DB에 기존의 Refresh 토큰 삭제 후 새로운 Refresh 토큰 저장
        refreshTokenRepository.deleteById(refreshTokenEntityId);
        addRefreshTokenEntity(userId, userName, newRefresh);

        // 기존 Refresh 토큰의 쿠기 만료
        ResponseCookie expiredRefreshTokenCookie = ResponseCookie.from("refresh", "")
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, expiredRefreshTokenCookie.toString());

        ResponseCookie refreshTokenCookie = jwtUtil.createCookie("refresh", newRefresh);
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        AccessTokenDto accessTokenDto = new AccessTokenDto(newAccess);
        return ResponseDto.builder()
                .statusCode(TOKEN_REISSUED_SUCCESS.getHttpStatus().value())
                .message(TOKEN_REISSUED_SUCCESS.getDetail())
                .data(accessTokenDto)
                .build();
    }

    public void addRefreshTokenEntity(Long userId, String userName, String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .userId(userId)
                .userName(userName)
                .refreshToken(refreshToken)
                .expiration(jwtUtil.getRefreshTokenExpireTime() / 1000)
                .build();

        refreshTokenRepository.save(refreshTokenEntity);
    }
}
