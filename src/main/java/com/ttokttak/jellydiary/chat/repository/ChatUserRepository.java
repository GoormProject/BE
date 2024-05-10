package com.ttokttak.jellydiary.chat.repository;

import com.ttokttak.jellydiary.chat.entity.ChatRoomEntity;
import com.ttokttak.jellydiary.chat.entity.ChatUserEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatUserRepository extends JpaRepository<ChatUserEntity, Long> {

    @Query("select cu.chatRoomId from ChatUserEntity cu where cu.userId = :loginUser and cu.chatRoomId IN (select chatRoomId from ChatUserEntity where userId = :targetUser)")
    List<ChatRoomEntity> findCommonChatRooms(@Param("loginUser")UserEntity loginUser, @Param("targetUser")UserEntity targetUser);


}
