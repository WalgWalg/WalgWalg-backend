package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, String> {
    Board findByUserAndTimestamp(User user, Date timestamp);
    Board findByUserAndTitle(User user, String title);
    List<Board> findByUser(User user);
    Board findByIdAndUser(String id ,User user);
}
