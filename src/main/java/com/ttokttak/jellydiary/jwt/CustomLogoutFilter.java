package com.ttokttak.jellydiary.jwt;

import com.ttokttak.jellydiary.exception.CustomException;
import com.ttokttak.jellydiary.user.entity.RefreshTokenEntity;
import com.ttokttak.jellydiary.user.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.*;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // path and method verify
        String requestUri = request.getRequestURI();
        if (!requestUri.matches("^\\/api\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

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

        // Refresh 토큰이 만료되었는지 확인(Validate)
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new CustomException(REFRESH_TOKEN_EXPIRED);
        }

        // Refresh 토큰의 category가 refresh인지 확인 (발급시 페이로드에 명시) (Verify)
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }

        // 들어온 Refresh 토큰이 가장 최근에 발급된 토큰인지 확인
        String refreshTokenEntityId = jwtUtil.getUserId(refresh) + ":" + refresh;
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findById(refreshTokenEntityId)
                .orElseThrow(() -> new CustomException(INVALID_REFRESH_TOKEN));

        // 쿠키에서 추출한 Refresh 토큰값이 RefreshTokenEntity의 refreshToken값과 같은지 확인
        if (!refreshTokenEntity.getRefreshToken().equals(refresh)) {
            throw new CustomException(INVALID_REFRESH_TOKEN);
        }

        // 로그아웃 진행
        // Refresh 토큰 DB에서 제거
        refreshTokenRepository.deleteById(refreshTokenEntityId);

        // 기존 Refresh 토큰의 쿠기 만료
        ResponseCookie expiredRefreshTokenCookie = ResponseCookie.from("refresh", "")
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, expiredRefreshTokenCookie.toString());

        response.setStatus(HttpStatus.OK.value());
    }
}