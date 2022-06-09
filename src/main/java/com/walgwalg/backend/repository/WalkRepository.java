package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

public interface WalkRepository extends JpaRepository<Walk,Long> {
    Walk findByUserAndWalkDate(User user, Date walkDate);
    Walk findByUserAndId(User user, Long id);
    List<Walk> findByUser(User user);
}
