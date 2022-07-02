package com.walgwalg.backend.repository;

import com.walgwalg.backend.entity.Park;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkRepository extends JpaRepository<Park, Long> {
    Park findByParkNameAndNumberAddress(String parkName, String numberAddress);
    List<Park> findByNumberAddressStartsWith(String numberAddress);
}
