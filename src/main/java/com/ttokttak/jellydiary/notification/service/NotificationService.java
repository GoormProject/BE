package com.ttokttak.jellydiary.notification.service;

import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(CustomOAuth2User customOAuth2User, String lastEventId);

    ResponseDto<?> getListNotification(CustomOAuth2User customOAuth2User);
}
