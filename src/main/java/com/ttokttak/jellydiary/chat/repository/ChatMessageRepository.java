package com.ttokttak.jellydiary.chat.repository;

import com.ttokttak.jellydiary.chat.entity.ChatMessageEntity;
import com.ttokttak.jellydiary.chat.entity.ChatRoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    ChatMessageEntity findTop1ByChatRoomIdOrderByCreatedAtDesc(ChatRoomEntity chatRoomId);

}
