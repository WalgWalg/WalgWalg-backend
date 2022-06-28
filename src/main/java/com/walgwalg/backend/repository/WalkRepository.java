package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface WalkRepository extends JpaRepository<Walk, String> {
    Walk findByUserAndWalkDate(User user, Date walkDate);

    Walk findByUserAndId(User user, String id);

    List<Walk> findByUser(User user);

    //sum(w.step_count)
//    @Query(value = "select w from Walk w inner join fetch w.user where w.user = :user and w.walkDate between :startDate and :endDate")

    @Query(value = "select w from Walk w inner join w.user where w.user = :user and w.walkDate between :startDate and :endDate")
    List<Walk> findByUserAndWalkDateBetween(Date startDate, Date endDate, User user);
//   @Query(value = "select new com.walgwalg.backend.web.dto.WalkTotal(w.stepCount) from Walk w inner join w.user where w.user = :user and w.walkDate between :startDate and :endDate group by w.user")
//   WalkTotal findByWalkDateBetweenAndUser(Date startDate, Date endDate, User user);
    @Query(value = "select sum(w.stepCount) from Walk w inner join w.user where w.user = :user and w.walkDate between :startDate and :endDate group by w.user")
    Integer findByStepCount(Date startDate, Date endDate, User user);
    @Query(value = "select sum(w.distance) from Walk w inner join w.user where w.user = :user and w.walkDate between :startDate and :endDate group by w.user")
    float findByDistance(Date startDate, Date endDate, User user);
//    Date findByWalkTime(Date startDate, Date endDate, User user);
//    @Query(value = "select sum(w.stepCount) from Walk w inner join w.user where w.user = :user and w.walkDate between :startDate and :endDate")
//    Integer findByWalkDateBetweenAndUser(Date startDate, Date endDate, User user);
}
