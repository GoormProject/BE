package com.ttokttak.jellydiary.user.respository;

import com.ttokttak.jellydiary.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
