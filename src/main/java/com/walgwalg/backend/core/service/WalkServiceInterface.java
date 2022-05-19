package com.walgwalg.backend.core.service;

import com.walgwalg.backend.web.dto.RequestWalk;
import com.walgwalg.backend.web.dto.ResponseGps;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface WalkServiceInterface {
    Long startWalk(String userid, Date walkDate, String location);
    void addGps(String userid, Date walkDate, Double latitude, Double longitude);
    List<ResponseGps.gps> getGps(String userId, Long walkId);
    void registerWalk(String userid, MultipartFile course, RequestWalk.registerWalk requestDto);
}
