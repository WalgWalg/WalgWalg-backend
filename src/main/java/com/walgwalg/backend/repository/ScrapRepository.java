package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Scrap;
import com.walgwalg.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Scrap findByUserAndBoard(User user, Board board);
    List<Scrap> findByUser(User user);
}
