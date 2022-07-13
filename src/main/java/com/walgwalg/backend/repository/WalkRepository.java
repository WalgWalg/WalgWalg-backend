package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Users;
import com.walgwalg.backend.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface WalkRepository extends JpaRepository<Walk, String> {
    Walk findByBoard(Board board);

    Walk findByUsersAndId(Users users, String id);

    List<Walk> findByUsers(Users users);

    @Query(value = "select w from Walk w inner join w.users where w.users = :users and w.walkDate between :startDate and :endDate")
    List<Walk> findByUsersAndWalkDateBetween(Date startDate, Date endDate, Users users);

    @Query(value = "select sum(w.stepCount) from Walk w inner join w.users where w.users = :users and w.walkDate between :startDate and :endDate group by w.users")
    Long findByStepCount(Date startDate, Date endDate, Users users);

    @Query(value = "select sum(w.distance) from Walk w inner join w.users where w.users = :users and w.walkDate between :startDate and :endDate group by w.users")
    Long findByDistance(Date startDate, Date endDate, Users users);

    List<Walk> findByAddressStartsWith(String numberAddress);
}
