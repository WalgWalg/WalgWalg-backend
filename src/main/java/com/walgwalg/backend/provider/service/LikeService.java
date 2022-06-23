package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.core.service.LikeServiceInterface;
import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.exception.errors.DuplicatedLikeException;
import com.walgwalg.backend.exception.errors.NotFoundBoardException;
import com.walgwalg.backend.exception.errors.NotFoundLikesException;
import com.walgwalg.backend.exception.errors.NotFoundUserException;
import com.walgwalg.backend.repository.BoardRepository;
import com.walgwalg.backend.repository.LikesRepository;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.web.dto.ResponseLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService implements LikeServiceInterface {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    @Override
    public void addLike(String userid, String boardId){
        User user = userRepository.findByUserid(userid);
        if(user == null){ //유저가 없을 경우
            throw new NotFoundUserException();
        }
        Board board = boardRepository.findById(boardId).orElseThrow(()->new NotFoundBoardException());

        Likes like = likesRepository.findByUserAndBoard(user, board);
        if(like != null){ //이미 좋아요 눌렀을 경우
            throw new DuplicatedLikeException();
        }
        //좋아요 추가
        like = Likes.builder()
                .user(user)
                .board(board)
                .build();
        like = likesRepository.save(like);
        user.addLikes(like);
        board.addLikes(like);
    }

    @Override
    @Transactional
    public List<ResponseLike.MyLike> listLikeBoard(String userid){
        User user = userRepository.findByUserid(userid); //유저 꺼내기
        if(user == null){//유저가 없으면
            throw new NotFoundUserException();
        }
        List<Likes> likeList = likesRepository.findByUser(user);//좋아요 리스트 꺼내기

        List<ResponseLike.MyLike> boards =new ArrayList<>();

        for(Likes like : likeList){
            Board board = like.getBoard();
            boards.add(ResponseLike.MyLike.of(board));
        }
        //Dto로 변환
        return boards;
    }

    @Transactional
    @Override
    public void deleteLikeBoard(String userid ,String boardId){
        User user = userRepository.findByUserid(userid);
        if(user == null){ //유저가 없을 경우
            throw new NotFoundUserException();
        }
        Board board = boardRepository.findById(boardId).orElseThrow(()->new NotFoundBoardException());
        Likes likes = likesRepository.findByUserAndBoard(user, board);
        if(likes == null){
            throw new NotFoundLikesException();
        }
        likesRepository.delete(likes);
    }

}
