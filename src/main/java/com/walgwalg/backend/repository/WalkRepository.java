package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface WalkRepository extends JpaRepository<Walk, String> {
    @Query(value = "select w from Walk w inner join w.user where w.user = :user and w.walkDate = CURRENT_DATE")
    List<Walk> findWalkWhenToday(User user);

    List<Walk> findByWalkDateContaining(String date);

    Walk findByUserAndId(User user, String id);

    List<Walk> findByUser(User user);

    @Query(value = "select w from Walk w inner join w.user where w.user = :user and w.walkDate between :startDate and :endDate")
    List<Walk> findByUserAndWalkDateBetween(Date startDate, Date endDate, User user);

    @Query(value = "select sum(w.stepCount) from Walk w inner join w.user where w.user = :user and w.walkDate between :startDate and :endDate group by w.user")
    Integer findByStepCount(Date startDate, Date endDate, User user);

    @Query(value = "select sum(w.distance) from Walk w inner join w.user where w.user = :user and w.walkDate between :startDate and :endDate group by w.user")
    Integer findByDistance(Date startDate, Date endDate, User user);
}
