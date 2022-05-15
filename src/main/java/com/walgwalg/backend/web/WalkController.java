package com.walgwalg.backend.web;

import com.walgwalg.backend.provider.security.JwtAuthToken;
import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import com.walgwalg.backend.provider.service.WalkService;
import com.walgwalg.backend.web.dto.RequestWalk;
import com.walgwalg.backend.web.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
        walkService.startWalk(userid, requestDto.getWalkDate(), requestDto.getLocation());
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("산책 기록 시작 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
