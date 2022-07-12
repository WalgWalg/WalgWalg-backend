package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUserid(String userid);
    Users findByNickname(String nickname);
    Users findByUseridAndPassword(String Userid, String password);
    Users findByRefreshToken(String token);
}
