package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.exception.errors.NotFoundBoardException;
import com.walgwalg.backend.repository.BoardRepository;
import com.walgwalg.backend.repository.LikesRepository;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.web.dto.ResponseLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class LikeServiceTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private LikeService likeService;
    @Autowired
    private LikesRepository likesRepository;

    @Test
    @Transactional
    @DisplayName("좋아요 추가 테스트")
    void addLikeTest(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .build();
        userRepository.save(user);
        //게시물 작성
        Board board = Board.builder()
                .title("title")
                .user(userRepository.findByUserid("userid"))
                .build();
        board = boardRepository.save(board);
        user.addBoard(board);

        //좋아요
        likeService.addLike("userid", board.getId());
        assertNotNull(likesRepository.findByUser(userRepository.findByUserid("userid")));
    }
    @Test
    @Transactional
    @DisplayName("좋아요 삭제 테스트")
    void deleteLike(){

        User user = User.builder()
                .userid("userid")
                .password("password")
                .build();
        userRepository.save(user);
        //게시물 작성
        Board board = Board.builder()
                .title("title")
                .user(userRepository.findByUserid("userid"))
                .build();
        boardRepository.save(board);
        user.addBoard(board);
        //좋아요
        Likes likes = Likes.builder()
                .user(user)
                .board(board)
                .build();
        likesRepository.save(likes);
        user.addLikes(likes);
        board.addLikes(likes);
        //좋아요 삭제
        likeService.deleteLikeBoard(board.getId());
        assertThrows(NotFoundBoardException.class,()->likeService.deleteLikeBoard(board.getId()));
    }


    @Test
    @Transactional
    @DisplayName("좋아요 리스트 테스트(성공)")
    void ListLikeBoardTest(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        user =  userRepository.save(user);
        //게시판
        Board board = Board.builder()
                .title("게시판")
                .build();
        board = boardRepository.save(board);
        Board board1 = Board.builder()
                .title("게시판1")
                .build();
        board1= boardRepository.save(board1);

        //좋아요
        Likes like = Likes.builder().board(board).user(user).build();
        likesRepository.save(like);
        Likes like1 = Likes.builder().board(board1).user(user).build();
        likesRepository.save(like1);
        //좋아요 리스트 출력
        assertNotNull(likeService.listLikeBoard("userid"));
    }
}
