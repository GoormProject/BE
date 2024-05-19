package com.ttokttak.jellydiary.notification.service;

import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter subscribe(CustomOAuth2User customOAuth2User, String lastEventId);
}
