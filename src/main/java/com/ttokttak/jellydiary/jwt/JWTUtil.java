package com.ttokttak.jellydiary.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private final SecretKey secretKey;

    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    private static final String BEARER_PREFIX = "Bearer ";

    public JWTUtil(
            @Value("${spring.jwt.secret}") String accessSecret,
            @Value("${jwt.access-token-expire-time}") long accessTokenExpireTime,
            @Value("${jwt.refresh-token-expire-time}") long refreshTokenExpireTime) {
        this.secretKey = new SecretKeySpec(accessSecret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }

    public long getRefreshTokenExpireTime() {
        return refreshTokenExpireTime;
    }

    // JWT 토큰에서 사용자 ID 추출
    public Long getUserId(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userId", Long.class);
    }

    // JWT 토큰에서 사용자 이름 추출
    public String getUserName(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userName", String.class);
    }

    // JWT 토큰에서 사용자의 역할 추출
    public String getAuthority(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("authority", String.class);
    }

    // JWT 토큰의 만료 여부 확인
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // JWT 토큰 판단용
    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    // Access Token 생성
    public String createAccessJwt(String category, Long userId, String userName, String authority) {
        return BEARER_PREFIX +
                Jwts.builder()
                .claim("category", category)
                .claim("userId", userId)
                .claim("userName", userName)
                .claim("authority", authority)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpireTime))
                .signWith(secretKey)
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshJwt(String category, Long userId, String userName, String authority) {
        return Jwts.builder()
                .claim("category", category)
                .claim("userId", userId)
                .claim("userName", userName)
                .claim("authority", authority)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpireTime))
                .signWith(secretKey)
                .compact();
    }

    public ResponseCookie createCookie(String key, String value) {
        ResponseCookie responseCookie = ResponseCookie.from(key, value)
                .maxAge(7 * 24 * 60 * 60) // 7일
//                .secure(true) // https 통신을 진행할 경우 활성화
                .path("/") // 쿠키가 적용될 범위
                .httpOnly(true) // 클라이언트단에서 자바스크립트로 해당 쿠키에 접근하지 못하도록 막음
                .build();

        return responseCookie;
    }

}
