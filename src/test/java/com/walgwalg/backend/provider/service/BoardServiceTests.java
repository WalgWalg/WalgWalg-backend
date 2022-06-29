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
    @Autowired
    private LikeService likeService;

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
    @DisplayName("게시판 삭제 테스트(좋아요가 있을 때)")
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
        likeService.addLike("userid", board.getId());
        //삭제
        boardService.deleteBoard("userid", board.getId());
        //해당 게시판의 좋아요도 다 삭제되어야 함!
        assertNull(boardRepository.findByUserAndTitle(user, "게시판 제목"));
        assertNull(likesRepository.findByUserAndBoard(user, board));
    }

    @Test
    @Transactional
    @DisplayName("좋아요 top5 게시판 조회 테스트")
    void getBoardTop(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .build();
        user = userRepository.save(user);
        User user1 = User.builder()
                .userid("userid1")
                .password("password")
                .build();
        user1 = userRepository.save(user1);
        User user2 = User.builder()
                .userid("userid2")
                .password("password")
                .build();
        user2 = userRepository.save(user2);
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
        likeService.addLike("userid", board.getId());
        //게시판 등록2
        Walk walk2 = Walk.builder()
                .user(user)
                .location("공원2")
                .build();
        walk2 = walkRepository.save(walk2);
        RequestBoard.register requestDto2 = RequestBoard.register.builder()
                .walkId(walk2.getId())
                .title("게시판 제목2")
                .contents("게시판 내용입니다.2")
                .build();
        boardService.registerBoard("userid", requestDto2);
        Board board2 = boardRepository.findByUserAndTitle(user, "게시판 제목2");
        //좋아요
        likeService.addLike("userid", board2.getId());
        likeService.addLike("userid1", board2.getId());
        List<ResponseBoard.top> list = boardService.getBoardTop();
        for (ResponseBoard.top response : list){
            System.out.println(response.getParkName());
        }
    }
    @Test
    @Transactional
    @DisplayName("전체 게시판 조회 테스트")
    void getAllBoard(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .build();
        user = userRepository.save(user);
        User user1 = User.builder()
                .userid("userid1")
                .password("password")
                .build();
        user1 = userRepository.save(user1);
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
        likeService.addLike("userid", board.getId());
        //게시판 등록2
        Walk walk2 = Walk.builder()
                .user(user1)
                .location("공원2")
                .build();
        walk2 = walkRepository.save(walk2);
        RequestBoard.register requestDto2 = RequestBoard.register.builder()
                .walkId(walk2.getId())
                .title("게시판 제목2")
                .contents("게시판 내용입니다.2")
                .build();
        boardService.registerBoard("userid1", requestDto2);
        Board board2 = boardRepository.findByUserAndTitle(user1, "게시판 제목2");
        //좋아요
        likeService.addLike("userid", board2.getId());
        likeService.addLike("userid1", board2.getId());
        List<ResponseBoard.getBoard> list =  boardService.getAllBoard();
        for(ResponseBoard.getBoard response : list){
            System.out.println(response.getLocation());
        }
    }
}
