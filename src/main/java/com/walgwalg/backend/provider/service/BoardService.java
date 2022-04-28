package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.core.service.BoardServiceInterface;
import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.Scrap;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.repository.BoardRepository;
import com.walgwalg.backend.repository.LikesRepository;
import com.walgwalg.backend.repository.ScrapRepository;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.web.dto.RequestBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        User boarduser = userRepository.findByUserid(requestDto.getUserid());
        Board board = boardRepository.findByUserAndTimestamp(boarduser, requestDto.getTimestamp());
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
}
