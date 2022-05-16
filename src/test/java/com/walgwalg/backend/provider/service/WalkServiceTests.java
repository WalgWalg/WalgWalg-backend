package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.Gps;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.entity.Walk;
import com.walgwalg.backend.repository.GpsRepository;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.repository.WalkRepository;
import com.walgwalg.backend.web.dto.RequestWalk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        walkService.startWalk("userid", date, "광교호수공원");
        assertNotNull(walkRepository.findAll());
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
        walkRepository.save(walk);
        //Gps 등록
        walkService.addGps("userid",date, 18.999,20.8888);
        assertNotNull(gpsRepository.findAll());
    }

    @Test
    @Transactional
    @DisplayName("산책 저장 테스트")
    void registerWalk() throws IOException {
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
        walkRepository.save(walk);

        RequestWalk.registerWalk registerWalk = RequestWalk.registerWalk.builder()
                .walkDate(date)
                .step_count("1000")
                .distance("1234")
                .calorie("400")
                .walkTime("24")
                .build();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());
        //산책 등록
        walkService.registerWalk("userid",mockMultipartFile, registerWalk);
    }
}
