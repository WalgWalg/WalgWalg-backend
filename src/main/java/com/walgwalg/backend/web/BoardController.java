package com.walgwalg.backend.web;

import com.walgwalg.backend.provider.security.JwtAuthToken;
import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import com.walgwalg.backend.provider.service.BoardService;
import com.walgwalg.backend.web.dto.RequestBoard;
import com.walgwalg.backend.web.dto.ResponseBoard;
import com.walgwalg.backend.web.dto.ResponseMessage;
import com.walgwalg.backend.web.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final BoardService boardService;

    @PostMapping("/board/add/like")
    public ResponseEntity<ResponseMessage> addLike(HttpServletRequest request,@Valid @RequestBody RequestBoard.like requestDto){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        boardService.addLike(userid, requestDto);
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("좋아요 추가 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/board/add/scrap")
    public ResponseEntity<ResponseMessage> addScrap(HttpServletRequest request, @Valid @RequestBody RequestBoard.scrap requestDto){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        boardService.addScrap(userid, requestDto);
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("스크랩 추가 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("user/list/board/like")
    public ResponseEntity<ResponseMessage> listLike(HttpServletRequest request){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid =null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        List<ResponseBoard.MyLike> list = boardService.listLikeBoard(userid);
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("좋아요 리스트 성공")
                .list(list)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
