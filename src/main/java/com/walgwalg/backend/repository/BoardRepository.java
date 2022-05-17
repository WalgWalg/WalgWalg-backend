package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByUserAndTimestamp(User user, Date timestamp);
}
