package com.ttokttak.jellydiary.chat.repository;

import com.ttokttak.jellydiary.chat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {

}
