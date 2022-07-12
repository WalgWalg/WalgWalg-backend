package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.Users;
import com.walgwalg.backend.entity.Walk;
import com.walgwalg.backend.repository.GpsRepository;
import com.walgwalg.backend.repository.UsersRepository;
import com.walgwalg.backend.repository.WalkRepository;
import com.walgwalg.backend.web.dto.RequestWalk;
import com.walgwalg.backend.web.dto.ResponseGps;
import com.walgwalg.backend.web.dto.ResponseWalk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
public class WalkServiceTests {
    @Autowired
    WalkService walkService;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    WalkRepository walkRepository;
    @Autowired
    GpsRepository gpsRepository;

    @Test
    @Transactional
    @DisplayName("산책 시작 테스트")
    void startWalkTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        usersRepository.save(user);

        Date date = new Date();
        Map<String, String> walkId = walkService.startWalk("userid", date,"공원","경기도 용인시");
        assertNotNull(walkId.get("walkId"));
    }

    @Test
    @Transactional
    @DisplayName("GPS 등록 테스트")
    void addGpsTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        usersRepository.save(user);
        Date date = new Date();
        Walk walk = Walk.builder()
                .users(user)
                .walkDate(date)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        //Gps 등록
        walkService.addGps("userid",walk.getId(), "18.999","20.8888");
        assertNotNull(gpsRepository.findAll());
    }
    @Test
    @Transactional
    @DisplayName("GPS 조회 테스트")
    void getGpsTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        usersRepository.save(user);
        Date date = new Date();
        Walk walk = Walk.builder()
                .users(user)
                .walkDate(date)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        //Gps 등록
        walkService.addGps("userid",walk.getId(), "18.999","20.8888");
        walkService.addGps("userid",walk.getId(), "28.999","30.8888");
        //Gps 조회
        List<ResponseGps.gps> list =walkService.getGps(walk.getId());
        for(ResponseGps.gps gps : list)
            System.out.println(gps.getLatitude() +" "+gps.getLongitude());
    }
    @Test
    @Transactional
    @DisplayName("산책 저장 테스트")
    void registerWalk() throws IOException, ParseException {
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        user = usersRepository.save(user);
        Date date = new Date();
        Walk walk = Walk.builder()
                .users(user)
                .walkDate(date)
                .location("광교호수공원")
                .build();
       walk = walkRepository.save(walk);

        RequestWalk.registerWalk registerWalk = RequestWalk.registerWalk.builder()
                .walkId(walk.getId())
                .step_count(1000)
                .distance(1234)
                .calorie(400)
                .walkTime("00:10:30")
                .build();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());
        //산책 등록
        walkService.registerWalk("userid",mockMultipartFile, registerWalk.getWalkId(),registerWalk.getStep_count(),
                registerWalk.getDistance(),registerWalk.getCalorie(),registerWalk.getWalkTime());
    }
    @Test
    @Transactional
    @DisplayName("산책 조회 테스트")
    void listWalk() throws ParseException {
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        usersRepository.save(user);
        Date date = new Date();
        Walk walk = Walk.builder()
                .users(user)
                .walkDate(date)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        RequestWalk.registerWalk registerWalk = RequestWalk.registerWalk.builder()
                .walkId(walk.getId())
                .step_count(1000)
                .distance(1234)
                .calorie(400)
                .walkTime("00:10:30")
                .build();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());
        //산책 등록
        walkService.registerWalk("userid",mockMultipartFile, registerWalk.getWalkId(),registerWalk.getStep_count(),
                registerWalk.getDistance(),registerWalk.getCalorie(),registerWalk.getWalkTime());
        //산책 조회
        List<ResponseWalk.list> list =walkService.getAllMyWalk("userid");
        assertNotNull(list);
    }
    @Test
    @Transactional
    @DisplayName("산책 삭제 테스트")
    void deleteWalk() throws IOException {
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        user = usersRepository.save(user);
        Date date = new Date();
        Walk walk = Walk.builder()
                .users(user)
                .walkDate(date)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);

        walkService.deleteWalk("userid", walk.getId());
        assertNull(walkRepository.findByUsersAndId(user, walk.getId()));
    }

    @Test
    @Transactional
    @DisplayName("일별 산책 조회 테스트")
    void getWalkForCalendar(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .build();
        user = usersRepository.save(user);
        Walk walk = Walk.builder()
                .users(user)
                .walkDate(new Date())
                .stepCount(1000)
                .distance(1234)
                .calorie(400)
                .walkTime("01:10")
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        Walk walk1 = Walk.builder()
                .users(user)
                .walkDate(new Date())
                .stepCount(300)
                .distance(124)
                .calorie(1230)
                .walkTime("01:10")
                .location("한강공원")
                .build();
        walk1 = walkRepository.save(walk1);
        Map<Date, ResponseWalk.calendar> response = walkService.getWalkForCalendar("userid");
        assertNotNull(response);
    }

    @Test
    @Transactional
    @DisplayName("산책 통계 조회 테스트")
    void getWalkTotalTest() throws ParseException{
        Users user = Users.builder()
                .userid("userid")
                .nickname("닉네임")
                .password("password")
                .build();
        user = usersRepository.save(user);
        Walk walk = Walk.builder()
                .users(user)
                .walkDate(Date.from(LocalDateTime.now().minusMonths(2).atZone(ZoneId.systemDefault()).toInstant()))
                .stepCount(1000)
                .distance(1234)
                .calorie(400)
                .walkTime("01:55")
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        Walk walk1 = Walk.builder()
                .users(user)
                .walkDate(new Date())
                .stepCount(300)
                .distance(124)
                .calorie(1230)
                .walkTime("01:10")
                .location("한강공원")
                .build();
        walk1 = walkRepository.save(walk1);

        Walk walk2 = Walk.builder()
                .users(user)
                .walkDate(Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant()))
                .stepCount(3009)
                .distance(124)
                .calorie(1230)
                .walkTime("01:55")
                .location("한강공원")
                .build();
        walk2 = walkRepository.save(walk2);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start = format.parse("2022-06-01");
        Date end = format.parse("2022-07-01");

        ResponseWalk.mainInfo response = walkService.getTotalWalk("userid");
        System.out.println(response);
    }

}
