package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.repository.BoardRepository;
import com.walgwalg.backend.repository.LikesRepository;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.web.dto.RequestBoard;
import com.walgwalg.backend.web.dto.RequestUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class BoardServiceTests {
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private  UserService userService;
    @Test
    @Transactional
    @DisplayName("좋아요 추가 테스트")
    void addLike(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .build();
        userRepository.save(user);
        //게시물 작성
        Board board = Board.builder()
                .title("title")
                .timestamp(new Date())
                .user(userRepository.findByUserid("userid"))
                .build();
        boardRepository.save(board);

        RequestBoard.like requestDto = RequestBoard.like.builder()
                .timestamp(board.getTimestamp())
                .user(board.getUser())
                .build();

        //좋아요
        boardService.addLike("userid", requestDto);

        System.out.println("좋아요 추가 성공");
        List<Likes> likeList = likesRepository.findByUser(userRepository.findByUserid("userid"));
        for(Likes likes :likeList)
            System.out.println(likes.getBoard().getTitle() +" "+ likes.getUser().getUserid());
    }

    @Test
    @Transactional
    @DisplayName("좋아요 취소 테스트")
    void deleteLike(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .build();
        userRepository.save(user);
        //게시물 작성
        Board board = Board.builder()
                .title("취소하는 게시판")
                .timestamp(new Date())
                .user(userRepository.findByUserid("userid"))
                .build();
        boardRepository.save(board);
        Board board1 = Board.builder()
                .title("취소 안하는 게시판")
                .timestamp(new Date())
                .user(userRepository.findByUserid("userid"))
                .build();
        boardRepository.save(board1);

        RequestBoard.like requestDto = RequestBoard.like.builder()
                .timestamp(board.getTimestamp())
                .user(board.getUser())
                .build();
        RequestBoard.like requestDto1 = RequestBoard.like.builder()
                .timestamp(board1.getTimestamp())
                .user(board1.getUser())
                .build();
        //좋아요 추가
        boardService.addLike("userid", requestDto);
        boardService.addLike("userid", requestDto1);
        //좋아요 취소
        boardService.addLike("userid", requestDto);
        List<Likes> likeList = likesRepository.findByUser(userRepository.findByUserid("userid"));
        for(Likes likes :likeList)
            System.out.println(likes.getBoard().getTitle() +" "+ likes.getUser().getUserid());
        System.out.println("좋아요 취소 성공");
    }
}
