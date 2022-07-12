package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.Users;
import com.walgwalg.backend.repository.BoardRepository;
import com.walgwalg.backend.repository.LikesRepository;
import com.walgwalg.backend.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class LikeServiceTests {
    @Autowired
    private UsersRepository usersRepository;
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
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .build();
        usersRepository.save(user);
        //게시물 작성
        Board board = Board.builder()
                .title("title")
                .users(usersRepository.findByUserid("userid"))
                .build();
        board = boardRepository.save(board);
        user.addBoard(board);

        //좋아요
        likeService.addLike("userid", board.getId());
        assertNotNull(likesRepository.findByUsers(usersRepository.findByUserid("userid")));
    }
    @Test
    @Transactional
    @DisplayName("좋아요 삭제 테스트")
    void deleteLike(){

        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .build();
        user = usersRepository.save(user);
        //게시물 작성
        Board board = Board.builder()
                .title("title")
                .users(usersRepository.findByUserid("userid"))
                .build();
        board = boardRepository.save(board);
        user.addBoard(board);
        //좋아요
        Likes likes = Likes.builder()
                .users(user)
                .board(board)
                .build();
        likes = likesRepository.save(likes);
        user.addLikes(likes);
        board.addLikes(likes);
        //좋아요 삭제
        likeService.deleteLikeBoard("userid",board.getId());
        assertNull(likesRepository.findByUsersAndBoard(user, board));
    }


    @Test
    @Transactional
    @DisplayName("좋아요 리스트 테스트(성공)")
    void ListLikeBoardTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        user =  usersRepository.save(user);
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
        Likes like = Likes.builder().board(board).users(user).build();
        likesRepository.save(like);
        Likes like1 = Likes.builder().board(board1).users(user).build();
        likesRepository.save(like1);
        //좋아요 리스트 출력
        assertNotNull(likeService.listLikeBoard("userid"));
    }
}
