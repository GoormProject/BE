package com.ttokttak.jellydiary.notification.entity;

public enum NotificationType {
    SUBSCRIBE_ACCEPT("님이 @ 구독하기 시작하였습니다.", "구독"),
    POST_LIKE_REQUEST("님이 @ 게시물을 좋아합니다.", "게시물 좋아요"),
    JOIN_REQUEST("님의 @ 다이어리 참여 요청이 도착하였습니다.", "다이어리"),
    JOIN_REJECT("님이 @ 회원님의 다이어리 참여 요청을 거절하였습니다.", "다이어리"),
    JOIN_ACCEPT("님이 @ 회원님의 다이어리 참여 요청을 수락하였습니다.", "다이어리"),
    JOIN_DELETE_REQUEST(" @ 다이어리가 삭제되었습니다.", "다이어리"),
    COMMENT_CREATE_REQUEST("님이 @ 게시물에 댓글을 남겼습니다.", "게시물 댓글"),
    REPLY_COMMENT_CREATE_REQUEST("님이 @ 회원님의 댓글에 답글을 남겼습니다.", "게시물 댓글"),
    POST_JOIN_CREATE_REQUEST("님과의 @ 다이어리에 새로운 게시물이 생성되었습니다.", "게시물"),
    POST_JOIN_DELETE_REQUEST("님과의 @ 다이어리에 게시물이 삭제되었습니다.", "게시물"),
    COMMENT_MENTION_CREATE_REQUEST("님이 @ 게시물 댓글에 회원님을 언급하셨습니다.", "언급"),
    FOLLOW_REQUEST("님이 @ 회원님을 팔로우합니다.", "새로운 팔로워"),
    DM_MESSAGE_REQUEST("님이 @ 회원님께 메시지를 보냈습니다.", "메시지(DM)");

    private String content;
    private String contentType;

    NotificationType(String content, String contentType) {
        this.content = content;
        this.contentType = contentType;
    }

    public String makeContent(String title) {
        return "'" + title + "'" + content;
    }

    public static String getContentType(NotificationEntity notification) {
        return notification.getNotificationType().contentType;
    }
}
