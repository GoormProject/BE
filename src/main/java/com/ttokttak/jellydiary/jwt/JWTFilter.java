package com.ttokttak.jellydiary.jwt;

import com.ttokttak.jellydiary.user.dto.UserOAuthDto;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.Authority;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.ACCESS_TOKEN_EXPIRED;
import static com.ttokttak.jellydiary.exception.message.ErrorMsg.INVALID_ACCESS_TOKEN;

// 로그인 후 요청이 있을 때마다 실행되는 JWT 검증 로직을 포함하고 있는 Spring Security 필터
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();

        if (requestUri.matches("^\\/login(?:\\/.*)?$") || requestUri.matches("^\\/oauth2(?:\\/.*)?$") || requestUri.equals("/api/reissue")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 Authorization 헤더를 찾음 (= accessToken)
        String authorization = request.getHeader("Authorization");

        // Authorization 헤더 검증, 토큰이 없다면 다음 필터로 넘김
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // 조건이 해당되면 메소드 종료 (필수)
        }

        // Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            response.setStatus(ACCESS_TOKEN_EXPIRED.getHttpStatus().value());
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(token);

        if (!category.equals("access")) {
            response.setStatus(INVALID_ACCESS_TOKEN.getHttpStatus().value());
            return;
        }

        // 토큰에서 userId, userName, authority 획득
        Long userId = jwtUtil.getUserId(token);
        String userName = jwtUtil.getUserName(token);
        String authority = jwtUtil.getAuthority(token);

        // UserOAuthDto를 생성하여 값 set
        UserOAuthDto userOAuthDto = new UserOAuthDto(userId, userName, authority);

        // UserDetails에 회원 정보 객체 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userOAuthDto);

        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        // 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
