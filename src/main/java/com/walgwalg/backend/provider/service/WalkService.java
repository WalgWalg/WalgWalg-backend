package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.core.service.WalkServiceInterface;
import com.walgwalg.backend.entity.Gps;
import com.walgwalg.backend.entity.Users;
import com.walgwalg.backend.entity.Walk;
import com.walgwalg.backend.exception.errors.NotFoundUserException;
import com.walgwalg.backend.exception.errors.NotFoundWalkException;
import com.walgwalg.backend.repository.GpsRepository;
import com.walgwalg.backend.repository.UsersRepository;
import com.walgwalg.backend.repository.WalkRepository;
import com.walgwalg.backend.web.dto.ResponseGps;
import com.walgwalg.backend.web.dto.ResponseWalk;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WalkService implements WalkServiceInterface {
    private final UsersRepository usersRepository;
    private final WalkRepository walkRepository;
    private final GpsRepository gpsRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public Map<String, String> startWalk(String userid, Date walkDate,String location, String address){
        Users user = usersRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }
         Walk walk = Walk.builder()
                .users(user)
                .walkDate(walkDate)
                .location(location)
                .address(address)
                .build();
        walk = walkRepository.save(walk);
        user.addWalk(walk);
        Map<String, String> map = new HashMap<>();
        map.put("walkId", walk.getId());
        return map;
    }
    @Override
    @Transactional
    public void addGps(String userid, String walkId, String latitude, String longitude){
        Users user = usersRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }
        Walk walk = walkRepository.findByUsersAndId(user, walkId);
        if(walk == null){
            throw new NotFoundWalkException();
        }
        Gps gps = Gps.builder()
                .latitude(latitude)
                .longitude(longitude)
                .walk(walk)
                .build();
        gpsRepository.save(gps);
        walk.addGps(gps);
    }
    @Override
    @Transactional
    public List<ResponseGps.gps> getGps(String walkId){
        Walk walk = walkRepository.findById(walkId).orElseThrow(()-> new NotFoundWalkException());
        List<Gps> gpsList = gpsRepository.findByWalk(walk);

        List<ResponseGps.gps> list = new ArrayList<>();
        for(Gps gps: gpsList){
            ResponseGps.gps gpsDto = ResponseGps.gps.builder()
                    .latitude(gps.getLatitude())
                    .longitude(gps.getLongitude())
                    .build();
            list.add(gpsDto);
        }
        return list;
    }
    @Override
    @Transactional
    public void registerWalk(String userid,MultipartFile course,String walkId, Long stepCount,
                             Long distance, Long calorie, String walkTime) throws ParseException {
        Users user = usersRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }
        Walk walk = walkRepository.findByUsersAndId(user, walkId);
        if(walk == null){
            throw new NotFoundWalkException();
        }
        //산책 코스 사진 s3에 등록
        String url="";
        try {
            url = s3Service.upload(course, "course");
        }catch (IOException e){
            System.out.println("s3 등록 실패");
        }

        //산책 추가 정보 등록
        walk.updateWalk(stepCount, distance, calorie,walkTime, url);
        user.addWalk(walk);
    }
    @Override
    @Transactional
    public List<ResponseWalk.list> getAllMyWalk(String userid){
        Users user = usersRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }
        List<Walk> walkList = walkRepository.findByUsers(user);
        List<ResponseWalk.list> list = new ArrayList<>();
        for(Walk walk : walkList){
            ResponseWalk.list responseDto = ResponseWalk.list.builder()
                    .id(walk.getId())
                    .walkDate(walk.getWalkDate())
                    .stepCount(walk.getStepCount())
                    .distance(walk.getDistance())
                    .calorie(walk.getCalorie())
                    .walkTime(walk.getWalkTime())
                    .course(walk.getCourse())
                    .location(walk.getLocation())
                    .build();
            list.add(responseDto);
        }
        return list;
    }

    @Override
    @Transactional
    public void deleteWalk(String userId, String walkId){
        Users user = usersRepository.findByUserid(userId);
        if(user == null){
            throw new NotFoundUserException();
        }
        Walk walk = walkRepository.findByUsersAndId(user, walkId);
        if(walk == null){
            throw new NotFoundWalkException();
        }
        walkRepository.delete(walk);
    }
    @Override
    @Transactional
    public Map<Date, ResponseWalk.calendar> getWalkForCalendar(String userId){
        Map<Date, ResponseWalk.calendar> map = new HashMap<>();

        Users user = usersRepository.findByUserid(userId);
        if(user == null){
            throw new NotFoundUserException();
        }
        List<Walk> walkList = walkRepository.findByUsers(user);
        for(Walk walk : walkList){
            ResponseWalk.calendar response = ResponseWalk.calendar.builder()
                    .distance(walk.getDistance())
                    .stepCount(walk.getStepCount())
                    .walkTime(walk.getWalkTime())
                    .build();
            map.put(walk.getWalkDate(),response);
        }
        return map;
    }

    @Transactional
    @Override
    public ResponseWalk.mainInfo getTotalWalk(String userid) throws ParseException{
        Map<String, ResponseWalk.total> map = new HashMap<>();
        Users user = usersRepository.findByUserid(userid);
        if(user == null){
            throw new NotFoundUserException();
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //일 총합
        LocalDate now = LocalDate.now();
        LocalDate next = LocalDate.now().plusDays(1);
        Date today = format.parse(now.toString());
        Date nextDate = format.parse(next.toString());
        List<Walk> todayWalkList = walkRepository.findByUsersAndWalkDateBetween(today, nextDate, user);

        ResponseWalk.total daily = ResponseWalk.total.builder()
                .stepCount(walkRepository.findByStepCount(today, today, user))
                .distance(walkRepository.findByDistance(today,today,user))
                .walkTime(getWalkTime(todayWalkList))
                .build();
        map.put("today", daily);
        //월 총합

        LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        Date startDate = format.parse(start.toString());
        Date endDate = format.parse(end.toString());
        List<Walk> monthWalkList = walkRepository.findByUsersAndWalkDateBetween(startDate, endDate, user);

            ResponseWalk.total month = ResponseWalk.total.builder()
                    .stepCount(walkRepository.findByStepCount(startDate, endDate, user))
                    .distance(walkRepository.findByDistance(startDate,endDate,user))
                    .walkTime(getWalkTime(monthWalkList))
                    .build();
            map.put("month", month);


        return ResponseWalk.mainInfo.builder()
                .nickName(user.getNickname())
                .walkTotal(map)
                .build();
    }
    private String getWalkTime(List<Walk> walkList){
        Integer hour =0;
        Integer minute =0;
        for(Walk walk :walkList){
            String[] time = walk.getWalkTime().split(":");
            hour+= Integer.parseInt(time[0]);
            minute+= Integer.parseInt(time[1]);
        }
        if(minute != 0){
            hour += minute/60;
            minute = minute%60;
        }
        return hour.toString()+"시간 "+minute.toString()+"분";
    }


}
