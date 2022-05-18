package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.core.service.BoardServiceInterface;
import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.Scrap;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.exception.errors.DuplicatedLikeException;
import com.walgwalg.backend.exception.errors.DuplicatedScrapException;
import com.walgwalg.backend.exception.errors.NotFoundBoardException;
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
        if(user == null){ //유저가 없을 경우
            throw new NotFoundUserException();
        }
        User writer = userRepository.findByUserid(requestDto.getWriterId()); // 좋아요 누른 게시판의 작성자
        if(user == null){//작성자가 없을 경우
            throw new NotFoundUserException();
        }
        Board board = boardRepository.findByUserAndTimestamp(writer, requestDto.getWriteDate()); //작성자와 작성일자를 기준으로 게시판 찾기
        if(board ==null){
            throw new NotFoundBoardException();
        }
        Likes like = likesRepository.findByUserAndBoard(user, board);
        if(like != null){ //이미 좋아요 눌렀을 경우
            throw new DuplicatedLikeException();
        }
        //좋아요 추가
        like = Likes.builder()
                .user(user)
                .board(board)
                .build();
        likesRepository.save(like);
        user.addLikes(like);
        board.addLikes(like);
    }
    @Transactional
    @Override
    public void deleteLikeBoard(Long boardId){
        Board board = boardRepository.findById(boardId).orElseThrow(()->new NotFoundBoardException());
        boardRepository.delete(board);
    }


    @Transactional
    @Override
    public void addScrap(String userid, RequestBoard.scrap requestDto){
        User user = userRepository.findByUserid(userid);
        if(user == null){ //유저가 없을 경우
            throw new NotFoundUserException();
        }
        User writer = userRepository.findByUserid(requestDto.getWriterId()); // 좋아요 누른 게시판의 작성자
        if(user == null){//작성자가 없을 경우
            throw new NotFoundUserException();
        }
        Board board = boardRepository.findByUserAndTimestamp(writer, requestDto.getWriteDate()); //작성자와 작성일자를 기준으로 게시판 찾기
        if(board ==null){
            throw new NotFoundBoardException();
        }
        Scrap scrap = scrapRepository.findByUserAndBoard(user, board);
        if(scrap != null){ //이미 스크랩 했을 경우
            throw new DuplicatedScrapException();
        }
        //스크랩 추가
       scrap = Scrap.builder()
                .user(user)
                .board(board)
                .build();
        scrapRepository.save(scrap);
        user.addScrap(scrap);
        board.addScrap(scrap);
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
            Board board = like.getBoard();
            boards.add(ResponseBoard.MyLike.of(board));
        }
        //Dto로 변환
        return boards;
    }
}
