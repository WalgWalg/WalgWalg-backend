package com.walgwalg.backend.web;

import com.walgwalg.backend.provider.security.JwtAuthToken;
import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import com.walgwalg.backend.provider.service.WalkService;
import com.walgwalg.backend.web.dto.RequestWalk;
import com.walgwalg.backend.web.dto.ResponseGps;
import com.walgwalg.backend.web.dto.ResponseMessage;
import com.walgwalg.backend.web.dto.ResponseWalk;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class WalkController {
    private final WalkService walkService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    @PostMapping("walk/start")
    public ResponseEntity<ResponseMessage> startWalk(HttpServletRequest request, @RequestBody RequestWalk.start requestDto){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        Long walkId=walkService.startWalk(userid, requestDto.getWalkDate(), requestDto.getLocation());
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("산책 기록 시작 성공")
                .list(walkId)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("walk/gps")
    public ResponseEntity<ResponseMessage> addGps(HttpServletRequest request, @RequestBody RequestWalk.addGps requestDto){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        walkService.addGps(userid, requestDto.getWalkDate(),requestDto.getLatitude(),requestDto.getLongitude());
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("gps 등록 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("walk/{walkId}/gps")
    public ResponseEntity<ResponseMessage> addGps(@PathVariable("walkId") Long walkId){
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
                                                        @RequestPart(value = "file", required = false) MultipartFile file,
                                                        @RequestPart(value = "requestDto") RequestWalk.registerWalk requestDto){
        System.out.println("-----------------------------------");
        System.out.println(requestDto.getWalkDate());
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        walkService.registerWalk(userid,file, requestDto);
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
}
