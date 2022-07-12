package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Scrap;
import com.walgwalg.backend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Scrap findByUsersAndBoard(Users users, Board board);
    List<Scrap> findByUsers(Users users);
}
