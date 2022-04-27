package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserid(String userid);
    User findByNickname(String nickname);
    User findByUseridAndPassword(String Userid, String password);

}
