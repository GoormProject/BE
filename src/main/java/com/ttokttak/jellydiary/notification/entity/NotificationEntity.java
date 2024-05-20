package com.ttokttak.jellydiary.notification.entity;

import com.ttokttak.jellydiary.user.entity.UserEntity;
import com.ttokttak.jellydiary.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notification")
public class NotificationEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @Column
    private NotificationContent content;

    @Column
    private Long returnId;

    @Column(nullable = false)
    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(nullable = false)
    private Long senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity receiver;

    @Builder
    public NotificationEntity(Long notificationId, String content, Long returnId, Boolean isRead, NotificationType notificationType, Long senderId, UserEntity receiver) {
        this.notificationId = notificationId;
        this.content = new NotificationContent(content);
        this.returnId = returnId;
        this.isRead = isRead;
        this.notificationType = notificationType;
        this.senderId = senderId;
        this.receiver = receiver;
    }

    public void read() {
        this.isRead = true;
    }
}
