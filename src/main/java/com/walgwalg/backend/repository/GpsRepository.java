package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.Gps;
import com.walgwalg.backend.entity.Walk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GpsRepository extends JpaRepository<Gps, Long> {
    List<Gps> findByWalk(Walk walk);
}
