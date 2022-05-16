package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.core.service.BoardServiceInterface;
import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.Scrap;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.exception.errors.NotFoundUserException;
import com.walgwalg.backend.repository.BoardRepository;
import com.walgwalg.backend.repository.LikesRepository;
import com.walgwalg.backend.repository.ScrapRepository;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.web.dto.RequestBoard;
import com.walgwalg.backend.web.dto.ResponseBoard;
import com.walgwalg.backend.web.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService implements BoardServiceInterface {
    private final LikesRepository likesRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;

    @Transactional
    @Override
    public void addLike(String userid, RequestBoard.like requestDto){
        User user = userRepository.findByUserid(userid);
        User boardUser = userRepository.findByUserid(requestDto.getUserid()); // 게시판 작성한 유저
        Board board = boardRepository.findByUserAndTimestamp(boardUser, requestDto.getTimestamp()); //유저와 작성일자를 기준으로 게시판 찾기
        Likes checklike = likesRepository.findByUserAndBoard(user, board);
        if(checklike != null){ //이미 있으면 좋아요 취소
            likesRepository.delete(checklike);
            return;
        }
        //좋아요 추가
        Likes like = Likes.builder()
                .user(user)
                .board(board)
                .build();
        likesRepository.save(like);
        user.addLikes(like);
    }

    @Transactional
    @Override
    public void addScrap(String userid, RequestBoard.scrap requestDto){
        User user = userRepository.findByUserid(userid);
        User boarduser = userRepository.findByUserid(requestDto.getUserid());
        Board board = boardRepository.findByUserAndTimestamp(boarduser, requestDto.getTimestamp());
        Scrap checkscrap = scrapRepository.findByUserAndBoard(user, board);
        if(checkscrap != null){ //이미 있으면 스크랩 취소
            scrapRepository.delete(checkscrap);
            return;
        }
        //스크랩 추가
       Scrap scrap = Scrap.builder()
                .user(user)
                .board(board)
                .build();
        scrapRepository.save(scrap);
    }
    @Override
    @Transactional
    public List<ResponseBoard.MyLike> listLikeBoard(String userid){
        User user = userRepository.findByUserid(userid); //유저 꺼내기
        if(user == null){//유저가 없으면
            throw new NotFoundUserException();
        }
        List<Likes> likeList = likesRepository.findByUser(user);//좋아요 리스트 꺼내기

        List<ResponseBoard.MyLike> boards =new ArrayList<>();

        for(Likes like : likeList){
            Board board = boardRepository.findByLikes(like);
            boards.add(ResponseBoard.MyLike.of(board));
        }
        //Dto로 변환
        return boards;
    }
}
