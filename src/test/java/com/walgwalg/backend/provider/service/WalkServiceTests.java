package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.Gps;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.entity.Walk;
import com.walgwalg.backend.repository.GpsRepository;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.repository.WalkRepository;
import com.walgwalg.backend.web.dto.RequestWalk;
import com.walgwalg.backend.web.dto.ResponseGps;
import com.walgwalg.backend.web.dto.ResponseWalk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class WalkServiceTests {
    @Autowired
    WalkService walkService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    WalkRepository walkRepository;
    @Autowired
    GpsRepository gpsRepository;

    @Test
    @Transactional
    @DisplayName("산책 시작 테스트")
    void startWalkTest(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        userRepository.save(user);

        Date date = new Date();
        Map<String, String> walkId = walkService.startWalk("userid", date);
        assertNotNull(walkId.get("walkId"));
    }

    @Test
    @Transactional
    @DisplayName("GPS 등록 테스트")
    void addGpsTest(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        userRepository.save(user);
        Date date = new Date();
        Walk walk = Walk.builder()
                .user(user)
                .walkDate(date)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        //Gps 등록
        walkService.addGps("userid",walk.getId(), 18.999,20.8888);
        assertNotNull(gpsRepository.findAll());
    }
    @Test
    @Transactional
    @DisplayName("GPS 조회 테스트")
    void getGpsTest(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        userRepository.save(user);
        Date date = new Date();
        Walk walk = Walk.builder()
                .user(user)
                .walkDate(date)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        //Gps 등록
        walkService.addGps("userid",walk.getId(), 18.999,20.8888);
        walkService.addGps("userid",walk.getId(), 28.999,30.8888);
        //Gps 조회
        List<ResponseGps.gps> list =walkService.getGps(walk.getId());
        for(ResponseGps.gps gps : list)
            System.out.println(gps.getLatitude() +" "+gps.getLongitude());
    }
    @Test
    @Transactional
    @DisplayName("산책 저장 테스트")
    void registerWalk() throws IOException, ParseException {
        User user = User.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        userRepository.save(user);
        Date date = new Date();
        Walk walk = Walk.builder()
                .user(user)
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
        User user = User.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        userRepository.save(user);
        Date date = new Date();
        Walk walk = Walk.builder()
                .user(user)
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
        User user = User.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        user = userRepository.save(user);
        Date date = new Date();
        Walk walk = Walk.builder()
                .user(user)
                .walkDate(date)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);

        walkService.deleteWalk("userid", walk.getId());
        assertNull(walkRepository.findByUserAndId(user, walk.getId()));
    }
}
