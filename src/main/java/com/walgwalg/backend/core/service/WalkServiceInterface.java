package com.walgwalg.backend.core.service;

import com.walgwalg.backend.web.dto.RequestWalk;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public interface WalkServiceInterface {
    void startWalk(String userid, Date walkDate, String location);
    void addGps(String userid, Date walkDate, Double latitude, Double longitude);
    void registerWalk(String userid, MultipartFile course, RequestWalk.registerWalk requestDto);
}
