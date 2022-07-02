package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    List<HashTag> findByBoard(Board board);
}
