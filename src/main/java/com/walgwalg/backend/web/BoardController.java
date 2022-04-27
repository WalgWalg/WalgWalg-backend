package com.walgwalg.backend.web;

import com.walgwalg.backend.provider.security.JwtAuthToken;
import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import com.walgwalg.backend.provider.service.BoardService;
import com.walgwalg.backend.web.dto.RequestBoard;
import com.walgwalg.backend.web.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final BoardService boardService;

    @PostMapping("/board/add/like")
    public ResponseEntity<ResponseMessage> addlike(HttpServletRequest request, RequestBoard.like requestDto){
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
}
