package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.core.service.BoardServiceInterface;
import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.repository.BoardRepository;
import com.walgwalg.backend.repository.LikesRepository;
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

    @Transactional
    @Override
    public void addLike(String userid, RequestBoard.like requestDto){
        User user = userRepository.findByUserid(userid);
        Board board = boardRepository.findByUserAndTimestamp(requestDto.getUser(), requestDto.getTimestamp());
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
}
