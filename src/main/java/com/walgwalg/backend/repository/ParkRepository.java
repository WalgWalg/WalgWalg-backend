package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.Park;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkRepository extends JpaRepository<Park, Long> {

}
