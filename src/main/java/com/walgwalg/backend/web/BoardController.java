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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final BoardService boardService;
    @PostMapping("/board")
    public ResponseEntity<ResponseMessage> registerBoard(HttpServletRequest request,@Valid @RequestBody RequestBoard.register requestDto){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        boardService.registerBoard(userid, requestDto);

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("게시판 등록 성공")
                .build(), HttpStatus.OK);
    }
    @GetMapping("/board/{boardId}")
    public ResponseEntity<ResponseMessage> getBoard(@PathVariable String boardId){
        ResponseBoard.getBoard response = boardService.getBoard(boardId);

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("게시판 조회 성공")
                .list(response)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/board/myBoard")
    public ResponseEntity<ResponseMessage> getMyBoard(HttpServletRequest request){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userId = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userId = jwtAuthToken.getClaims().getSubject();
        }

        List<ResponseBoard.list> response = boardService.getMyBoard(userId);

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("내가 작성한 게시판 조회 성공")
                .list(response)
                .build(), HttpStatus.OK);
    }
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<ResponseMessage> deleteBoard(HttpServletRequest request, @PathVariable String boardId){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userId = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userId = jwtAuthToken.getClaims().getSubject();
        }

        boardService.deleteBoard(userId, boardId);

        return new ResponseEntity<>(ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("게시판 삭제 성공")
                .build(), HttpStatus.OK);
    }
}
