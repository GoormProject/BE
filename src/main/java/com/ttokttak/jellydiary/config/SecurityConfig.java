package com.ttokttak.jellydiary.config;

import com.ttokttak.jellydiary.jwt.CustomLogoutFilter;
import com.ttokttak.jellydiary.jwt.JWTFilter;
import com.ttokttak.jellydiary.jwt.JWTUtil;
import com.ttokttak.jellydiary.user.handler.CustomSuccessHandler;
import com.ttokttak.jellydiary.user.repository.RefreshTokenRepository;
import com.ttokttak.jellydiary.user.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // cors 설정
        http
            .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://apic.app", "https://jellydiary.life", "https://api.jellydiary.life"));
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
                    return configuration;
                }
            }));

        //csrf disable
        http
            .csrf((auth) -> auth.disable());

        // From 로그인 방식 disable(기본 login form 비활성화)
        http
            .formLogin(AbstractHttpConfigurer::disable);

        // HTTP Basic 인증 방식 disable
        http
            .httpBasic((auth) -> auth.disable());

        // JWTFilter 추가
        http
//            .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
            .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);

        http
            .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshTokenRepository), LogoutFilter.class);

        // oauth2
        http
            .oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                    .userService(customOAuth2UserService))
                .successHandler(customSuccessHandler));

        // 경로별 인가 작업
        http
            .authorizeHttpRequests((auth) -> auth
                    //.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()  // CORS pre-flight 요청 허용
                    //.requestMatchers("/ws/**").permitAll()  // 웹소켓 경로 허용
                .requestMatchers("**").permitAll()
                .anyRequest().authenticated());

        // 세션 설정 : STATELESS
        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}