package com.walgwalg.backend.web;

import com.walgwalg.backend.provider.security.JwtAuthToken;
import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import com.walgwalg.backend.provider.service.ScrapService;
import com.walgwalg.backend.web.dto.ResponseMessage;
import com.walgwalg.backend.web.dto.ResponseScrap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ScrapController {
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final ScrapService scrapService;

    @PostMapping("/scrap")
    public ResponseEntity<ResponseMessage> addScrap(HttpServletRequest request, @RequestBody Map<String, String> boardId){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        scrapService.addScrap(userid, boardId.get("boardId"));
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("스크랩 추가 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/scrap")
    public ResponseEntity<ResponseMessage> getScrap(HttpServletRequest request){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        List<ResponseScrap.MyScrap>list = scrapService.getScrap(userid);
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("스크랩 조회 성공")
                .list(list)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/scrap/{boardId}")
    public ResponseEntity<ResponseMessage> deleteScrap(HttpServletRequest request, @PathVariable String boardId){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid =null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        scrapService.deleteScrap(userid, boardId);

        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("스크랩 삭제")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
