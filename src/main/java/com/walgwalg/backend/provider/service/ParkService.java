package com.walgwalg.backend.provider.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walgwalg.backend.entity.Park;
import com.walgwalg.backend.repository.ParkRepository;
import com.walgwalg.backend.web.dto.ParkInfo;
import com.walgwalg.backend.web.dto.RequestPark;
import com.walgwalg.backend.web.dto.ResponsePark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkService {
    private final ParkRepository parkRepository;

    @Transactional
    public void registerPark(RequestPark requestDto){
        for(ParkInfo requestPark : requestDto.getPark()){
            Park park = Park.builder()
                    .parkName(requestPark.getParkName())
                    .roadAddress(requestPark.getRoadAddress())
                    .numberAddress(requestPark.getNumberAddress())
                    .latitude(requestPark.getLatitude())
                    .longitude(requestPark.getLongitude())
                    .build();
            parkRepository.save(park);
        }
        }
        @Transactional
        public List<ResponsePark.getAll> getAllPark(){
            List<ResponsePark.getAll> list = new ArrayList<>();
            List<Park> parkList = parkRepository.findAll();
            for (Park park : parkList){
                ResponsePark.getAll response = ResponsePark.getAll.builder()
                        .parkName(park.getParkName())
                        .roadAddress(park.getRoadAddress())
                        .numberAddress(park.getNumberAddress())
                        .latitude(park.getLatitude())
                        .longitude(park.getLongitude())
                        .build();
                list.add(response);
            }
            return list;
        }

        @Transactional
        public void deleteAllPark(){
            parkRepository.deleteAll();
        }
}
