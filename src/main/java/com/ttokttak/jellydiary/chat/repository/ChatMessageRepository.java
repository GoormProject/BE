package com.ttokttak.jellydiary.chat.repository;

import com.ttokttak.jellydiary.chat.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

}
