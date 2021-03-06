package com.walgwalg.backend.core.service;

import com.walgwalg.backend.web.dto.RequestWalk;
import com.walgwalg.backend.web.dto.ResponseGps;
import com.walgwalg.backend.web.dto.ResponseWalk;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WalkServiceInterface {
    Map<String, String> startWalk(String userid, Date walkDate,String location, String address);
    void addGps(String userid, String walkId, String latitude, String longitude);
    List<ResponseGps.gps> getGps(String walkId);
    void registerWalk(String userid,MultipartFile course,String walkId, Long step_count,Long distance, Long calorie, String walkTime) throws ParseException;
    void deleteWalk(String userId, String walkId);
    List<ResponseWalk.list> getAllMyWalk(String userid);
    Map<Date, ResponseWalk.calendar> getWalkForCalendar(String userid);
    ResponseWalk.mainInfo getTotalWalk(String userid)throws ParseException;
}
