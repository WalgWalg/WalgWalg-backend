package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Scrap;
import com.walgwalg.backend.entity.Users;
import com.walgwalg.backend.repository.BoardRepository;
import com.walgwalg.backend.repository.ScrapRepository;
import com.walgwalg.backend.repository.UsersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
public class ScrapServiceTests {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ScrapService scrapService;
    @Autowired
    private ScrapRepository scrapRepository;


    @Test
    @Transactional
    @DisplayName("스크랩 추가 테스트")
    void addScrapTest(){
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

        //스크랩
        scrapService.addScrap("userid", board.getBoardId());
        assertNotNull(scrapRepository.findByUsers(usersRepository.findByUserid("userid")));
    }

    @Test
    @Transactional
    @DisplayName("스크랩 조회 테스트(성공)")
    void getScrapTest(){
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

        //스크랩
        Scrap scrap = Scrap.builder().board(board).users(user).build();
        scrapRepository.save(scrap);
        Scrap scrap1 = Scrap.builder().board(board1).users(user).build();
        scrapRepository.save(scrap1);
        //좋아요 리스트 출력
        assertNotNull(scrapService.getScrap("userid"));
    }

    @Test
    @Transactional
    @DisplayName("스크랩 삭제 테스트")
    void deleteScrap(){
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
        //스크랩
        Scrap scrap = Scrap.builder()
                .users(user)
                .board(board)
                .build();
        scrap = scrapRepository.save(scrap);
        user.addScrap(scrap);
        board.addScrap(scrap);
        //스크랩 삭제
        scrapService.deleteScrap("userid",board.getBoardId());
        assertNull(scrapRepository.findByUsersAndBoard(user, board));
    }
}
