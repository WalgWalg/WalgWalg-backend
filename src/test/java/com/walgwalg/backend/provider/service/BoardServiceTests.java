package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.*;
import com.walgwalg.backend.exception.errors.NotFoundBoardException;
import com.walgwalg.backend.repository.*;
import com.walgwalg.backend.web.dto.RequestBoard;
import com.walgwalg.backend.web.dto.ResponseBoard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BoardServiceTests {
    @Autowired
    private BoardService boardService;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private ScrapRepository scrapRepository;
    @Autowired
    private WalkRepository walkRepository;
    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private  UserService userService;
    @Autowired
    private LikeService likeService;

    @Test
    @Transactional
    @DisplayName("게시판 등록 테스트")
    void registerBoardTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .build();
        usersRepository.save(user);
        Walk walk = Walk.builder()
                .users(user)
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

        assertNotNull(boardRepository.findByUsers(user));
    }
    @Test
    @Transactional
    @DisplayName("게시판 수정 테스트")
    void updateBoardTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .build();
        usersRepository.save(user);
        Walk walk = Walk.builder()
                .users(user)
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
        Board board = boardRepository.findByUsersAndTitle(user, "게시판 제목");
        //게시판 수정
        RequestBoard.update update = RequestBoard.update.builder()
                .boardId(board.getId())
                .title("수정된 제목")
                .contents("내용")
                .build();
        boardService.updateBoard("userid", update);
        Board board1 = boardRepository.findById(board.getId()).orElseThrow(()->new NotFoundBoardException());
        List<HashTag> hashTagList = hashTagRepository.findByBoard(board);
        assertEquals(0, hashTagList.size());
        assertNotNull(boardRepository.findByUsers(user));
    }
    @Test
    @Transactional
    @DisplayName("게시판 조회 테스트")
    void getBoardTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .build();
        user = usersRepository.save(user);
        Walk walk = Walk.builder()
                .users(user)
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
        Board board = boardRepository.findByUsersAndTitle(user,"게시판 제목");
        ResponseBoard.getBoard response = boardService.getBoard(board.getId());
        assertNotNull(response);
    }

    @Test
    @Transactional
    @DisplayName("내가 작성한 게시판 조회 테스트")
    void getMyBoardTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .build();
        user = usersRepository.save(user);
        //게시판 등록1
        Walk walk = Walk.builder()
                .users(user)
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
                .users(user)
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
    void deleteBoardTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .build();
        user = usersRepository.save(user);
        //게시판 등록1
        Walk walk = Walk.builder()
                .users(user)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        RequestBoard.register requestDto = RequestBoard.register.builder()
                .walkId(walk.getId())
                .title("게시판 제목")
                .contents("내용입니다.")
                .build();
        boardService.registerBoard("userid", requestDto);
        Board board = boardRepository.findByUsersAndTitle(user, "게시판 제목");
        //삭제
        boardService.deleteBoard("userid", board.getId());
        assertNull(boardRepository.findByUsersAndTitle(user, "게시판 제목"));
    }
    @Test
    @Transactional
    @DisplayName("게시판 삭제 테스트(좋아요가 있을 때)")
    void deleteBoardWhenExistLikesTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .build();
        user = usersRepository.save(user);
        //게시판 등록1
        Walk walk = Walk.builder()
                .users(user)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        RequestBoard.register requestDto = RequestBoard.register.builder()
                .walkId(walk.getId())
                .title("게시판 제목")
                .contents("내용입니다.")
                .build();
        boardService.registerBoard("userid", requestDto);
        Board board = boardRepository.findByUsersAndTitle(user, "게시판 제목");
        //좋아요
        likeService.addLike("userid", board.getId());
        //삭제
        boardService.deleteBoard("userid", board.getId());
        //해당 게시판의 좋아요도 다 삭제되어야 함!
        assertNull(boardRepository.findByUsersAndTitle(user, "게시판 제목"));
        assertNull(likesRepository.findByUsersAndBoard(user, board));
    }

    @Test
    @Transactional
    @DisplayName("좋아요 top5 게시판 조회 테스트")
    void getBoardTopTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .build();
        user = usersRepository.save(user);
        Users user1 = Users.builder()
                .userid("userid1")
                .password("password")
                .build();
        user1 = usersRepository.save(user1);
        Users user2 = Users.builder()
                .userid("userid2")
                .password("password")
                .build();
        user2 = usersRepository.save(user2);
        //게시판 등록1
        Walk walk = Walk.builder()
                .users(user)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        RequestBoard.register requestDto = RequestBoard.register.builder()
                .walkId(walk.getId())
                .title("게시판 제목")
                .contents("내용입니다.")
                .build();
        boardService.registerBoard("userid", requestDto);
        Board board = boardRepository.findByUsersAndTitle(user, "게시판 제목");
        //좋아요
        likeService.addLike("userid", board.getId());
        //게시판 등록2
        Walk walk2 = Walk.builder()
                .users(user)
                .location("공원2")
                .build();
        walk2 = walkRepository.save(walk2);
        RequestBoard.register requestDto2 = RequestBoard.register.builder()
                .walkId(walk2.getId())
                .title("게시판 제목2")
                .contents("게시판 내용입니다.2")
                .build();
        boardService.registerBoard("userid", requestDto2);
        Board board2 = boardRepository.findByUsersAndTitle(user, "게시판 제목2");
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
    void getAllBoardTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .build();
        user = usersRepository.save(user);
        Users user1 = Users.builder()
                .userid("userid1")
                .password("password")
                .build();
        user1 = usersRepository.save(user1);
        //게시판 등록1
        Walk walk = Walk.builder()
                .users(user)
                .location("광교호수공원")
                .build();
        walk = walkRepository.save(walk);
        RequestBoard.register requestDto = RequestBoard.register.builder()
                .walkId(walk.getId())
                .title("게시판 제목")
                .contents("내용입니다.")
                .build();
        boardService.registerBoard("userid", requestDto);
        Board board = boardRepository.findByUsersAndTitle(user, "게시판 제목");
        //좋아요
        likeService.addLike("userid", board.getId());
        //게시판 등록2
        Walk walk2 = Walk.builder()
                .users(user1)
                .location("공원2")
                .build();
        walk2 = walkRepository.save(walk2);
        RequestBoard.register requestDto2 = RequestBoard.register.builder()
                .walkId(walk2.getId())
                .title("게시판 제목2")
                .contents("게시판 내용입니다.2")
                .build();
        boardService.registerBoard("userid1", requestDto2);
        Board board2 = boardRepository.findByUsersAndTitle(user1, "게시판 제목2");
        //좋아요
        likeService.addLike("userid", board2.getId());
        likeService.addLike("userid1", board2.getId());
        List<ResponseBoard.list> list =  boardService.getAllBoard();
        for(ResponseBoard.list response : list){
            System.out.println(response.getDate());
        }
    }
    @Test
    @Transactional
    @DisplayName("위치 기반 게시판 조회 테스트")
    void getBoardInRegionTest(){
        Users user = Users.builder()
                .userid("userid")
                .password("password")
                .build();
        user = usersRepository.save(user);
        Users user1 = Users.builder()
                .userid("userid1")
                .password("password")
                .build();
        user1 = usersRepository.save(user1);
        //게시판 등록1
        Walk walk = Walk.builder()
                .users(user)
                .location("광교호수공원")
                .address("경기도 용인시 처인구")
                .build();
        walk = walkRepository.save(walk);
        RequestBoard.register requestDto = RequestBoard.register.builder()
                .walkId(walk.getId())
                .title("게시판 제목")
                .contents("내용입니다.")
                .build();
        boardService.registerBoard("userid", requestDto);
        //게시판 등록2
        Walk walk2 = Walk.builder()
                .users(user1)
                .location("공원2")
                .address("경기도 안양시 동안구")
                .build();
        walk2 = walkRepository.save(walk2);
        RequestBoard.register requestDto2 = RequestBoard.register.builder()
                .walkId(walk2.getId())
                .title("게시판 제목2")
                .contents("게시판 내용입니다.2")
                .build();
        boardService.registerBoard("userid1", requestDto2);

        List<ResponseBoard.list> list =  boardService.getBoardInRegion("경기도 용인시 처인구");
        assertEquals(1, list.size());
    }
}
