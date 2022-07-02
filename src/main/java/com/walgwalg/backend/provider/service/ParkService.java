package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.Park;
import com.walgwalg.backend.exception.errors.DuplicatedParkException;
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
            for (Park item : parkList){
                ResponsePark.getAll response = ResponsePark.getAll.builder()
                        .parkName(item.getParkName())
                        .roadAddress(item.getRoadAddress())
                        .numberAddress(item.getNumberAddress())
                        .latitude(item.getLatitude())
                        .longitude(item.getLongitude())
                        .build();
                list.add(response);
            }
            return list;
        }

        @Transactional
        public void deleteAllPark(){
            parkRepository.deleteAll();
        }

        @Transactional
        public List<ResponsePark.getPark> getParkInRegion(String region){
            List<Park> parkList =parkRepository.findByNumberAddressStartsWith(region);
            List<ResponsePark.getPark> list = new ArrayList<>();
            for(Park park : parkList){
                ResponsePark.getPark response = ResponsePark.getPark.builder()
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
}
