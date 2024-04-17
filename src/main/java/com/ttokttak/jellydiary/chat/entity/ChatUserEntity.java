package com.ttokttak.jellydiary.chat.entity;

import com.ttokttak.jellydiary.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "chat_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomUserId;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoomEntity chatRoomId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

}
