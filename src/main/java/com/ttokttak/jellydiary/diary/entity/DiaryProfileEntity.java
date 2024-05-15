package com.ttokttak.jellydiary.diary.entity;

import com.ttokttak.jellydiary.chat.entity.ChatRoomEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE diary_profile SET is_diary_deleted = true WHERE diary_id = ?")
@Table(name = "diary_profile")
public class DiaryProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    @Column(nullable = false)
    private String diaryName;

    private String diaryDescription;

    private String diaryProfileImage;

    @Column(nullable = false)
    private Boolean isDiaryDeleted;

    @OneToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoomEntity chatRoomId;

    @Builder
    public DiaryProfileEntity(Long diaryId, String diaryName, String diaryDescription, String diaryProfileImage, Boolean isDiaryDeleted, ChatRoomEntity chatRoomId) {
        this.diaryId = diaryId;
        this.diaryName = diaryName;
        this.diaryDescription = diaryDescription;
        this.diaryProfileImage = diaryProfileImage;
        this.isDiaryDeleted = false;
        this.chatRoomId = chatRoomId;
    }

    public void diaryProfileUpdate(String diaryName, String diaryDescription) {
        this.diaryName = diaryName;
        this.diaryDescription = diaryDescription;
    }

    public void diaryProfileImgUpdate(String diaryProfileImage) {
        this.diaryProfileImage = diaryProfileImage;
    }

    public void assignChatRoom(ChatRoomEntity chatRoom) {
        this.chatRoomId = chatRoom;
    }

}
