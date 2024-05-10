package com.ttokttak.jellydiary.chat.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "chat_room")
public class ChatRoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @Column(nullable = false)
    private String chatRoomName;

    @Builder
    public ChatRoomEntity(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }
}
