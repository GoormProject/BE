package com.ttokttak.jellydiary.chat.repository;

import com.ttokttak.jellydiary.chat.entity.ChatRoomEntity;
import com.ttokttak.jellydiary.chat.entity.ChatUserEntity;
import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatUserRepository extends JpaRepository<ChatUserEntity, Long> {

    @Query("select cu.chatRoomId from ChatUserEntity cu where cu.userId = :loginUser and cu.chatRoomId IN (select chatRoomId from ChatUserEntity where userId = :targetUser)")
    List<ChatRoomEntity> findCommonChatRooms(@Param("loginUser")UserEntity loginUser, @Param("targetUser")UserEntity targetUser);

    Optional<ChatUserEntity> findByChatRoomIdAndUserId(ChatRoomEntity chatRoomEntity, UserEntity userEntity);

    @Query("select cu.chatRoomId from ChatUserEntity cu where cu.userId = :userId")
    List<ChatRoomEntity> findChatRoomsByUserId(@Param("userId") UserEntity userId);

    @Query("select cu.userId from ChatUserEntity cu where cu.chatRoomId.chatRoomId = :chatRoomId")
    List<UserEntity> findUsersByChatRoomId(@Param("chatRoomId") Long chatRoomId);

}
