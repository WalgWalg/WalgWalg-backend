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
    public ResponseEntity<ResponseMessage> getBoard(@PathVariable Long boardId){
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
    public ResponseEntity<ResponseMessage> deleteBoard(HttpServletRequest request, @PathVariable Long boardId){
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
    //좋아요
    @PostMapping("/board/like")
    public ResponseEntity<ResponseMessage> addLike(HttpServletRequest request, @RequestBody Map<String, Long> boardId){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        boardService.addLike(userid, boardId.get("boardId"));
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("좋아요 추가 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("board/like")
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
    @DeleteMapping("/board/like/{boardId}")
    public ResponseEntity<ResponseMessage> deleteLike(@PathVariable Long boardId){
        boardService.deleteLikeBoard(boardId);

        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("게시판 좋아요 삭제")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    //스크랩
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

}
