package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.Scrap;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.repository.BoardRepository;
import com.walgwalg.backend.repository.LikesRepository;
import com.walgwalg.backend.repository.ScrapRepository;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.web.dto.RequestBoard;
import com.walgwalg.backend.web.dto.ResponseBoard;
import com.walgwalg.backend.web.dto.ResponseUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

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
        user.addBoard(board);
        RequestBoard.like requestDto = RequestBoard.like.builder()
                .writeDate(board.getTimestamp())
                .writerId(board.getUser().getUserid())
                .build();

        //좋아요
        boardService.addLike("userid", requestDto);
        assertNotNull(likesRepository.findByUser(userRepository.findByUserid("userid")));
        }

    @Test
    @Transactional
    @DisplayName("스크랩 추가 테스트")
    void addScrap(){
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

        RequestBoard.scrap requestDto = RequestBoard.scrap.builder()
                .writeDate(board.getTimestamp())
                .writerId(board.getUser().getUserid())
                .build();

        //스크랩
        boardService.addScrap("userid", requestDto);
        assertNotNull(scrapRepository.findByUser(userRepository.findByUserid("userid")));
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
