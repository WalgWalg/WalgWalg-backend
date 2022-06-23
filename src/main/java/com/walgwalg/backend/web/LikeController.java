package com.walgwalg.backend.web;

import com.walgwalg.backend.provider.security.JwtAuthToken;
import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import com.walgwalg.backend.provider.service.LikeService;
import com.walgwalg.backend.web.dto.ResponseBoard;
import com.walgwalg.backend.web.dto.ResponseLike;
import com.walgwalg.backend.web.dto.ResponseMessage;
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
public class LikeController {
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final LikeService likeService;
    @PostMapping("/like")
    public ResponseEntity<ResponseMessage> addLike(HttpServletRequest request, @RequestBody Map<String, String> boardId){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        likeService.addLike(userid, boardId.get("boardId"));
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("좋아요 추가 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/like")
    public ResponseEntity<ResponseMessage> listLike(HttpServletRequest request){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid =null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        List<ResponseLike.MyLike> list = likeService.listLikeBoard(userid);
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("좋아요 리스트 성공")
                .list(list)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/like/{boardId}")
    public ResponseEntity<ResponseMessage> deleteLike(@PathVariable String boardId){
        likeService.deleteLikeBoard(boardId);

        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("게시판 좋아요 삭제")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
