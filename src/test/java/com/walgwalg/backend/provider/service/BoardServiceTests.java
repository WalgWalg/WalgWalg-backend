package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.*;
import com.walgwalg.backend.exception.errors.NotFoundBoardException;
import com.walgwalg.backend.repository.*;
import com.walgwalg.backend.web.dto.RequestBoard;
import com.walgwalg.backend.web.dto.ResponseBoard;
import com.walgwalg.backend.web.dto.ResponseUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    private ScrapRepository scrapRepository;
    @Autowired
    private WalkRepository walkRepository;
    @Autowired
    private  UserService userService;

    @Test
    @Transactional
    @DisplayName("게시판 등록 테스트")
    void registerBoardTest(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .build();
        userRepository.save(user);
        Walk walk = Walk.builder()
                .user(user)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        List<String> hashTags = new ArrayList<>();
        hashTags.add("해시태그1");
        hashTags.add("해시태그2");
        RequestBoard.register requestDto = RequestBoard.register.builder()
                .walkId(walk.getId())
                .title("게시판 제목")
                .contents("내용입니다.")
                .hashTags(hashTags)
                .build();
        boardService.registerBoard("userid", requestDto);

        assertNotNull(boardRepository.findByUser(user));
    }
    @Test
    @Transactional
    @DisplayName("게시판 조회 테스트")
    void getBoardTest(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .build();
        user = userRepository.save(user);
        Walk walk = Walk.builder()
                .user(user)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        List<String> hashTags = new ArrayList<>();
        hashTags.add("해시태그1");
        hashTags.add("해시태그2");
        RequestBoard.register requestDto = RequestBoard.register.builder()
                .walkId(walk.getId())
                .title("게시판 제목")
                .contents("내용입니다.")
                .hashTags(hashTags)
                .build();
        boardService.registerBoard("userid", requestDto);
        Board board = boardRepository.findByUserAndTitle(user,"게시판 제목");
        ResponseBoard.getBoard response = boardService.getBoard(board.getId());
        assertNotNull(response);
    }

    @Test
    @Transactional
    @DisplayName("내가 작성한 게시판 조회 테스트")
    void getMyBoardTest(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .build();
        user = userRepository.save(user);
        //게시판 등록1
        Walk walk = Walk.builder()
                .user(user)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        List<String> hashTags = new ArrayList<>();
        hashTags.add("해시태그1");
        hashTags.add("해시태그2");
        RequestBoard.register requestDto = RequestBoard.register.builder()
                .walkId(walk.getId())
                .title("게시판 제목")
                .contents("내용입니다.")
                .hashTags(hashTags)
                .build();

        boardService.registerBoard("userid", requestDto);
        //게시판 등록2 - 해시태그 없을 때
        Walk walk1 = Walk.builder()
                .user(user)
                .location("중앙공원")
                .build();
        walk1 = walkRepository.save(walk1);
        RequestBoard.register requestDto1 = RequestBoard.register.builder()
                .walkId(walk1.getId())
                .title("게시판 제목1")
                .contents("내용입니다.")
                .build();
        boardService.registerBoard("userid", requestDto1);
        //조회
        List<ResponseBoard.list> list = boardService.getMyBoard("userid");
        System.out.println(list);
    }
    @Test
    @Transactional
    @DisplayName("게시판 삭제 테스트")
    void deleteBoard(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .build();
        user = userRepository.save(user);
        //게시판 등록1
        Walk walk = Walk.builder()
                .user(user)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        RequestBoard.register requestDto = RequestBoard.register.builder()
                .walkId(walk.getId())
                .title("게시판 제목")
                .contents("내용입니다.")
                .build();
        boardService.registerBoard("userid", requestDto);
        Board board = boardRepository.findByUserAndTitle(user, "게시판 제목");
        //삭제
        boardService.deleteBoard("userid", board.getId());
        assertNull(boardRepository.findByUserAndTitle(user, "게시판 제목"));
    }
    @Test
    @Transactional
    @DisplayName("게시판 삭제 테스트")
    void deleteBoardWhenExistLikes(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .build();
        user = userRepository.save(user);
        //게시판 등록1
        Walk walk = Walk.builder()
                .user(user)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        RequestBoard.register requestDto = RequestBoard.register.builder()
                .walkId(walk.getId())
                .title("게시판 제목")
                .contents("내용입니다.")
                .build();
        boardService.registerBoard("userid", requestDto);
        Board board = boardRepository.findByUserAndTitle(user, "게시판 제목");
        //좋아요
        boardService.addLike("userid", board.getId());
        //삭제
        boardService.deleteBoard("userid", board.getId());
        assertNull(boardRepository.findByUserAndTitle(user, "게시판 제목"));
        assertNull(likesRepository.findByUserAndBoard(user, board));
    }
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
        boardService.addLike("userid", board.getId());
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
        boardService.deleteLikeBoard(board.getId());
        assertThrows(NotFoundBoardException.class,()->boardService.deleteLikeBoard(board.getId()));
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
        userRepository.save(user);
        //게시판
        Board board = Board.builder()
                .title("게시판")
                .build();
        boardRepository.save(board);
        Board board1 = Board.builder()
                .title("게시판1")
                .build();
        boardRepository.save(board1);

        //좋아요
        Likes like = Likes.builder().board(board).user(user).build();
        likesRepository.save(like);
        Likes like1 = Likes.builder().board(board1).user(user).build();
        likesRepository.save(like1);
        //좋아요 리스트 출력
        System.out.println("좋아요 리스트");
        List<ResponseBoard.MyLike> list = boardService.listLikeBoard("userid");
        for(ResponseBoard.MyLike myLike : list)
            System.out.println(myLike.getTitle());
    }
}
