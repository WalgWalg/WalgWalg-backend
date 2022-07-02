package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.Park;
import com.walgwalg.backend.exception.errors.DuplicatedParkException;
import com.walgwalg.backend.repository.ParkRepository;
import com.walgwalg.backend.web.dto.ParkInfo;
import com.walgwalg.backend.web.dto.RequestPark;
import com.walgwalg.backend.web.dto.ResponsePark;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class ParkServiceTests {
    @Autowired
    private ParkService parkService;
    @Autowired
    private ParkRepository parkRepository;

    @Test
    @Transactional
    @DisplayName("공원 전체 등록 테스트(성공)")
    void registerParkAll(){
        List<ParkInfo> parkInfoList = new ArrayList<>();
        ParkInfo parkInfo1 = ParkInfo.builder()
                .parkName("공원1")
                .numberAddress("경기도 안양시")
                .build();
        parkInfoList.add(parkInfo1);
        ParkInfo parkInfo2 = ParkInfo.builder()
                .parkName("공원2")
                .numberAddress("경기도 안양시")
                .build();
        parkInfoList.add(parkInfo2);
        RequestPark requestPark = RequestPark.builder()
                .park(parkInfoList)
                .build();
        parkService.registerPark(requestPark);
    }

    @Test
    @Transactional
    @DisplayName("지역별 공원 조회 테스트(성공)")
    void getParkInRegion(){
        Park park = Park.builder()
                .parkName("처인구 공원")
                .numberAddress("경기도 용인시 처인구 백암면 근창리 101-8번지일원")
                .build();
        parkRepository.save(park);

        Park park1 = Park.builder()
                .parkName("기흥구 공원")
                .numberAddress("경기도 용인시 기흥구 보정동 1270번지")
                .build();
        parkRepository.save(park1);

        Park park2 = Park.builder()
                .parkName("처인구 공원2")
                .numberAddress("경기도 용인시 처인구 역북동 385-5번지 일원")
                .build();
        parkRepository.save(park2);

        //지역별 조회
        String region = "경기도 용인시 처인구";
        List<ResponsePark.getPark> list = parkService.getParkInRegion(region);
        assertEquals(list.size(), 2);

    }
}
