package com.ttokttak.jellydiary.chat.entity;

import com.ttokttak.jellydiary.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "chat_message")
public class ChatMessageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatMessageId;

    @Column(nullable = false)
    private String chatMessage;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoomEntity chatRoomId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;

    @Builder
    public ChatMessageEntity(String chatMessage, ChatRoomEntity chatRoomId, UserEntity userId) {
        this.chatMessage = chatMessage;
        this.chatRoomId = chatRoomId;
        this.userId = userId;
    }
}
