package com.ttokttak.jellydiary.notification.entity;

import java.util.ArrayList;
import java.util.List;

public enum NotificationType {
    SUBSCRIBE_ACCEPT("님이 @ 구독하기 시작하였습니다.", "구독"),
    POST_LIKE_REQUEST("님이 @ 게시물을 좋아합니다.", "게시물 좋아요"),
    JOIN_REQUEST("님의 @ 다이어리 참여 요청이 도착하였습니다.", "다이어리"),
    JOIN_REJECT("님이 @ 회원님의 다이어리 참여 요청을 거절하였습니다.", "다이어리"),
    JOIN_ACCEPT("님이 @ 회원님의 다이어리 참여 요청을 수락하였습니다.", "다이어리"),
    JOIN_DELETE_REQUEST("님과의 @ 다이어리가 삭제되었습니다.", "다이어리"),
    COMMENT_CREATE_REQUEST("님이 @ 게시물에 댓글을 남겼습니다.", "게시물 댓글"),
    POST_JOIN_CREATE_REQUEST("님과의 @ 다이어리에 새로운 게시물이 생성되었습니다.", "게시물"),
    POST_JOIN_DELETE_REQUEST("님과의 @ 다이어리에 게시물이 삭제되었습니다.", "게시물"),
    COMMENT_MENTION_CREATE_REQUEST("님이 @ 게시물 댓글에 회원님을 언급하셨습니다..", "언급"),
    FOLLOW_REQUEST("님이 @ 회원님을 팔로우합니다.", "새로운 팔로워"),
    DM_MESSAGE_REQUEST("님이 @ 회원님을께 메시지를 보냈습니다.", "메시지(DM)");

    private String content;
    private String contentType;

    NotificationType(String content, String contentType) {
        this.content = content;
        this.contentType = contentType;
    }

    public String makeContent(String title) {
        return "'" + title + "'" + content;
    }

//    public static List<NotificationType> userContent() {
//        List<NotificationType> notificationTypes = new ArrayList<>();
//        notificationTypes.add(FRIEND_ACCEPT);
//        notificationTypes.add(SUBSCRIBE_ACCEPT);
//        notificationTypes.add(FRIEND_REQUEST);
//        return notificationTypes;
//    }

    public static String contentType(NotificationType notificationType) {
        if (notificationType.equals(SUBSCRIBE_ACCEPT)) {
            return "구독";
        } else if (notificationType.equals(POST_LIKE_REQUEST)) {
            return "게시물 좋아요";
        } else if (notificationType.equals(COMMENT_CREATE_REQUEST)) {
            return "게시물 댓글";
        } else if (notificationType.equals(COMMENT_MENTION_CREATE_REQUEST)) {
            return "언급";
        } else if (notificationType.equals(FOLLOW_REQUEST)) {
            return "새로운 팔로워";
        } else if (notificationType.equals(DM_MESSAGE_REQUEST)) {
            return "메시지(DM)";
        } else if (notificationType.equals(POST_JOIN_CREATE_REQUEST) || notificationType.equals(POST_JOIN_DELETE_REQUEST)) {
            return "게시물";
        } else {
            return "다이어리";
        }
    }



    public static String getContentType(NotificationEntity notification) {
        return notification.getNotificationType().contentType;
    }


}
