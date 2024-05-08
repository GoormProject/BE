package com.ttokttak.jellydiary.user.repository;

import com.ttokttak.jellydiary.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByOauthId(String oauthId);

    List<UserEntity> findAllByUserNameContains(String searchWord);
}
