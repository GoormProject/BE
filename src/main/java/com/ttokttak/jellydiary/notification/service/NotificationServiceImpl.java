package com.ttokttak.jellydiary.notification.service;

import com.ttokttak.jellydiary.diary.repository.DiaryProfileRepository;
import com.ttokttak.jellydiary.diarypost.repository.DiaryPostRepository;
import com.ttokttak.jellydiary.notification.entity.NotificationEntity;
import com.ttokttak.jellydiary.notification.entity.NotificationType;
import com.ttokttak.jellydiary.notification.mapper.NotificationMapper;
import com.ttokttak.jellydiary.notification.repository.NotificationRepository;
import com.ttokttak.jellydiary.notification.repository.SseRepository;
import com.ttokttak.jellydiary.user.dto.oauth2.CustomOAuth2User;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.user.repository.UserRepository;
import com.ttokttak.jellydiary.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.ttokttak.jellydiary.exception.message.ErrorMsg.UNAUTHORIZED_MEMBER;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserRepository userRepository;
    private final DiaryProfileRepository diaryProfileRepository;
    private final DiaryPostRepository diaryPostRepository;
    private final NotificationMapper notificationMapper;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 20;
    private final NotificationRepository notificationRepository;
    private final SseRepository sseRepository;


    @Override
    public SseEmitter subscribe(CustomOAuth2User customOAuth2User, String lastEventId) {
        Long userId = customOAuth2User.getUserId();

        String emitterId = makeTimeIncludeId(userId);
        // lastEventId가 있을 경우, userId와 비교해서 유실된 데이터일 경우 재전송할 수 있다.

        sseRepository.deleteAllEmitterStartWithId(String.valueOf(userId));

        SseEmitter emitter = sseRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> {
            log.info("SSE 연결 Complete");
            sseRepository.deleteById(emitterId);
//            onClientDisconnect(emitter, "Compeletion");
        });
        //시간이 만료된 경우 자동으로 레포지토리에서 삭제하고 클라이언트에서 재요청을 보낸다.
        emitter.onTimeout(() -> {
            log.info("SSE 연결 Timeout");
            sseRepository.deleteById(emitterId);
//            onClientDisconnect(emitter, "Timeout");
        });
        emitter.onError((e) -> sseRepository.deleteById(emitterId));
        //Dummy 데이터를 보내 503에러 방지. (SseEmitter 유효시간 동안 어느 데이터도 전송되지 않으면 503에러 발생)
        String eventId = makeTimeIncludeId(userId);
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + userId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방한다.
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, userId, emitterId, emitter);
        }

        return emitter;
    }

    public void send(Long senderId, Long receiverId, NotificationType notificationType, String content, Long returnId) {
        NotificationEntity notification = notificationRepository.save(createNotification(senderId, receiverId, notificationType, content, returnId));

        String strReceiverId = String.valueOf(receiverId);
        String eventId = strReceiverId + "_" + System.currentTimeMillis();
        Map<String, SseEmitter> emitters = sseRepository.findAllEmitterStartWithByUserId(strReceiverId);

//        Long user = null;
//        Long post = null;

//        if(NotificationType.userContent().contains(notificationType)) {
//            user = returnId;
//        } else {
//            post = returnId;
//        }

//        Long finalPost = post;
//        Long finalUser = user;
        emitters.forEach(
                (key, emitter) -> {
                    sseRepository.saveEventCache(key, notification);
//                    sendNotification(emitter, eventId, key, new NotificationResponseDto(notification, NotificationType.getContentType(notification), notification.getContent(), notification.getReceiver().getUserId(), finalUser)
                    sendNotification(emitter, eventId, key, notificationMapper.entityToNotificationResponseDto(notification, returnId));
                }
        );
    }

    private String makeTimeIncludeId(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    //알림 전송
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            sseRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Long userId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = sseRepository.findAllEventCacheStartWithByUserId(String.valueOf(userId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }

    //알림 생성
    private NotificationEntity createNotification(Long senderId, Long receiverId, NotificationType notificationType, String content, Long returnId) {
        UserEntity receiver = userRepository.findById(receiverId).orElseThrow();
        return NotificationEntity.builder()
                .receiver(receiver)
                .senderId(senderId)
                .notificationType(notificationType)
                .content(content)
                .returnId(returnId)
                .isRead(false)
                .build();
    }
}
