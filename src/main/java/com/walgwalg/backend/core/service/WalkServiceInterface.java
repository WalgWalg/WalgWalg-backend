package com.walgwalg.backend.core.service;

import com.walgwalg.backend.web.dto.RequestWalk;
import com.walgwalg.backend.web.dto.ResponseGps;
import com.walgwalg.backend.web.dto.ResponseWalk;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface WalkServiceInterface {
    Long startWalk(String userid, Date walkDate, String location);
    void addGps(String userid, Date walkDate, Double latitude, Double longitude);
    List<ResponseGps.gps> getGps(Long walkId);
    void registerWalk(String userid,MultipartFile course, RequestWalk.registerWalk requestDto);
    List<ResponseWalk.list> getAllMyWalk(String userid);
}
