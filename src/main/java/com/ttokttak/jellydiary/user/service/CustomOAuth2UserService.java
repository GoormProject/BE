package com.ttokttak.jellydiary.user.service;

import com.ttokttak.jellydiary.user.dto.UserOAuthDto;
import com.ttokttak.jellydiary.user.dto.oauth2.*;
import com.ttokttak.jellydiary.user.entity.Authority;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.entity.UserStateEnum;
import com.ttokttak.jellydiary.user.mapper.UserMapper;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService  {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // loadUser 메소드를 호출하여 인증 서버로부터 액세스 토큰과 사용자 정보를 포함한 OAuth2User 객체를 얻음
        OAuth2User oAuth2User = super.loadUser(userRequest);
//        String refreshToken = (String) userRequest.getAdditionalParameters().get("refresh_token");

        // OAuth2 Provider를 식별하기 위하여 registrationId를 획득
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response;

        // Provider에 맞게 OAuth2Response 객체를 생성
        // => OAuth2Response interface를 이용해 Provider에 맞게 구현클래스를 만들어서 사용
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            oAuth2Response = null;
            return null;
        }

        // 리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String oauthId = oAuth2Response.getProvider()+"_"+oAuth2Response.getProviderId();

        // 난수를 사용자 이름에 추가
        String randomPart = UUID.randomUUID().toString().split("-")[0]; // UUID에서 첫 부분만 사용
        String userNameWithRandom = oAuth2Response.getName() + "_" + randomPart;

        // 사용자 정보를 데이터베이스에서 검색 => 유저가 검색되지 않으면 새로운 유저 정보를 DB에 저장
        UserEntity userEntity = userRepository.findByOauthId(oauthId)
                .orElseGet(() -> UserEntity.builder()
                        .oauthId(oauthId)
                        .providerType(oAuth2Response.getProvider())
                        .providerToken(null)
                        .userName(userNameWithRandom)
                        .authority(Authority.USER)
                        .userState(UserStateEnum.ACTIVE)
                        .notificationSetting(true)
                        .build());

        UserEntity saveUserEntity = userRepository.save(userEntity);

        // UserEntity를 UserOAuthDto로 변환
        UserOAuthDto userOAuthDto = UserMapper.INSTANCE.entityToUserOAuthDto(saveUserEntity);

        return new CustomOAuth2User(userOAuthDto);
    }
}