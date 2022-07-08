package com.walgwalg.backend.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.walgwalg.backend.provider.security.JwtAuthToken;
import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import com.walgwalg.backend.provider.service.WalkService;
import com.walgwalg.backend.web.dto.RequestWalk;
import com.walgwalg.backend.web.dto.ResponseGps;
import com.walgwalg.backend.web.dto.ResponseMessage;
import com.walgwalg.backend.web.dto.ResponseWalk;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class WalkController {
    private final WalkService walkService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    @PostMapping("walk/start")
    public ResponseEntity<ResponseMessage> startWalk(HttpServletRequest request, @Valid @RequestBody RequestWalk.start requestDto){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        Map<String, String> walkId = walkService.startWalk(userid, requestDto.getWalkDate(), requestDto.getLocation(), requestDto.getAddress());
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("산책 기록 시작 성공")
                .list(walkId)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("walk/gps")
    public ResponseEntity<ResponseMessage> addGps(HttpServletRequest request, @Valid @RequestBody RequestWalk.addGps requestDto){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        walkService.addGps(userid, requestDto.getWalkId(),requestDto.getLatitude(),requestDto.getLongitude());
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("gps 등록 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("walk/{walkId}/gps")
    public ResponseEntity<ResponseMessage> getGps(@PathVariable String walkId){
        List<ResponseGps.gps> gpsList = walkService.getGps(walkId);
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("gps 조회 성공")
                .list(gpsList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/walk/finish")
    public ResponseEntity<ResponseMessage> registerWalk(HttpServletRequest request,
                                                        @RequestPart MultipartFile file,
                                                        @Valid @RequestPart RequestWalk.registerWalk requestDto){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        try{
            walkService.registerWalk(userid,file, requestDto.getWalkId(), requestDto.getStep_count(),requestDto.getDistance(), requestDto.getCalorie(),requestDto.getWalkTime());

        }catch (ParseException e){
            System.out.println("산책 시간 타입 변경 실패");
        }
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("산책 등록 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/walk/list")
    public ResponseEntity<ResponseMessage> getAllMyWalk(HttpServletRequest request){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        List<ResponseWalk.list>list = walkService.getAllMyWalk(userid);
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("산책 조회 성공")
                .list(list)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/walk/{walkId}")
    public ResponseEntity<ResponseMessage> deleteWalk(HttpServletRequest request, @PathVariable String walkId ){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userId = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userId = jwtAuthToken.getClaims().getSubject();
        }
        walkService.deleteWalk(userId, walkId);

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("산책 삭제 성공")
                .build(), HttpStatus.OK);
    }
    @GetMapping("/walk/calendar")
    public ResponseEntity<ResponseMessage> getWalkForCalendar(HttpServletRequest request){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userId = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userId = jwtAuthToken.getClaims().getSubject();
        }
        Map<Date, ResponseWalk.calendar> list = walkService.getWalkForCalendar(userId);
        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("일별 산책 성공")
                .list(list)
                .build(), HttpStatus.OK);

    }

    @GetMapping("/walk/total")
    public ResponseEntity<ResponseMessage> getTotalWalk(HttpServletRequest request){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userId = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userId = jwtAuthToken.getClaims().getSubject();
        }
        ResponseWalk.mainInfo response = null;
        try {
            response = walkService.getTotalWalk(userId);
        }catch (ParseException e){
            System.out.println("산책 통계 조회 실패");
        }

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("산책 통계 조회 성공")
                .list(response)
                .build(), HttpStatus.OK);

    }

}
