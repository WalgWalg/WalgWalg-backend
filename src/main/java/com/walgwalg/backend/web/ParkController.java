package com.walgwalg.backend.web;

import com.walgwalg.backend.provider.service.ParkService;
import com.walgwalg.backend.web.dto.RequestPark;
import com.walgwalg.backend.web.dto.ResponseMessage;
import com.walgwalg.backend.web.dto.ResponsePark;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParkController {
    private final ParkService parkService;

    @PostMapping("/park/all")
    public ResponseEntity<ResponseMessage> registerPark(@RequestBody RequestPark request){
        parkService.registerPark(request);
        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("공원 공공데이터 등록 성공")
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/park/all")
    public ResponseEntity<ResponseMessage> deleteAllPark(){
        parkService.deleteAllPark();
        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("공원 공공데이터 전체 삭제 성공")
                .build(), HttpStatus.OK);
    }
    @GetMapping("/park")
    public ResponseEntity<ResponseMessage> getAllPark(){
        List<ResponsePark.getAll> list = parkService.getAllPark();
        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("공원 공공데이터 전체 조회 성공")
                .list(list)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/park/{region}")
    public ResponseEntity<ResponseMessage> getParkInRegion(@PathVariable String region){
        List<ResponsePark.getPark> list = parkService.getParkInRegion(region);
        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("지역별 공원 공공데이터 조회 성공")
                .list(list)
                .build(), HttpStatus.OK);
    }
}
