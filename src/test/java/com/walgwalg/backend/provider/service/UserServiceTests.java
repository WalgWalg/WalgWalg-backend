package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.exception.errors.LoginFailedException;
import com.walgwalg.backend.exception.errors.NotFoundUserException;
import com.walgwalg.backend.exception.errors.RegisterFailedException;
import com.walgwalg.backend.repository.BoardRepository;
import com.walgwalg.backend.repository.LikesRepository;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.web.dto.RequestUser;
import com.walgwalg.backend.web.dto.ResponseUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private LikesRepository likeRepository;

    @Test
    @Transactional
    @DisplayName("회원가입 테스트(실패- 아이디 중복)")
    void registerWhenDuplicatedUseridTest(){
        User user = User.builder()
                .userid("test")
                .password("test")
                .build();
        userRepository.save(user);
        //회원가입
        RequestUser.register register = RequestUser.register.builder()
                .userid("test")
                .password("test")
                .nickname("nickname")
                .address("address")
                .build();
        System.out.println("회원가입 실패-아이디 중복");
        assertThrows(RegisterFailedException.class,()->userService.register(register));
    }

    @Test
    @Transactional
    @DisplayName("회원가입 테스트(실패- 닉네임 중복)")
    void registerWhenDuplicatedNicknameTest(){
        User user = User.builder()
                .userid("userid")
                .password("test")
                .nickname("nick")
                .build();
        userRepository.save(user);
        //회원가입
        RequestUser.register register = RequestUser.register.builder()
                .userid("test")
                .password("test")
                .nickname("nick")
                .address("address")
                .build();
        System.out.println("회원가입 실패-닉네임 중복");
        assertThrows(RegisterFailedException.class,()->userService.register(register));
    }

    @Test
    @Transactional
    @DisplayName("회원가입 테스트(성공)")
    void registerTest(){
        RequestUser.register register = RequestUser.register.builder()
                .userid("userid")
                .password("password")
                .nickname("nickname")
                .address("address")
                .build();
        userService.register(register);
        System.out.println("회원가입 성공");
        User user = userRepository.findByUserid("userid");
        System.out.println(user.getUserid() +" "+user.getNickname()+" "+user.getAddress());
    }
    @Test
    @Transactional
    @DisplayName("로그인 테스트(실패-아이디가 없음)")
    void loginWhenNotExistUseridTest(){
        RequestUser.login login = RequestUser.login.builder()
                .userid("test")
                .password("test")
                .build();
        System.out.println("로그인 실패-아이디 없음");
        assertThrows(LoginFailedException.class,()->userService.login(login));
    }
    @Test
    @Transactional
    @DisplayName("로그인 테스트(실패-비밀번호가 맞지 않음)")
    void loginWhenNotCorrectPasswordTest(){
        RequestUser.register register = RequestUser.register.builder()
                .userid("userid")
                .password("password")
                .nickname("nickname")
                .address("address")
                .build();
        userService.register(register);
        //로그인
        RequestUser.login login = RequestUser.login.builder()
                .userid("userid")
                .password("test")
                .build();
        System.out.println("로그인 실패-비밀번호가 맞지 않음");
        assertThrows(LoginFailedException.class,()->userService.login(login));
    }
    @Test
    @Transactional
    @DisplayName("로그인 테스트(성공)")
    void loginTest(){
        RequestUser.register register = RequestUser.register.builder()
                .userid("userid")
                .password("password")
                .nickname("nickname")
                .address("address")
                .build();
        userService.register(register);
        //로그인
        RequestUser.login login = RequestUser.login.builder()
                .userid("userid")
                .password("password")
                .build();
        ResponseUser.login response = userService.login(login).orElseThrow(()-> new LoginFailedException());
        System.out.println("로그인 성공");
        System.out.println(response.getAccessToken());
        System.out.println(response.getRefreshToken());

        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }
    @Test
    @Transactional
    @DisplayName("회원정보 수정 테스트(실패- 회원 정보가 없음)")
    void ChangeUserInfoWhenNotExistUserTest(){
        RequestUser.changeInfo changeInfoDto = RequestUser.changeInfo.builder()
                .nickname("hello")
                .build();
        System.out.println("회원정보 수정 테스트(실패- 회원 정보가 없음)");
        assertThrows(NotFoundUserException.class,()->userService.changeUserInfo("test",changeInfoDto));
    }
    @Test
    @Transactional
    @DisplayName("회원정보 수정 테스트(실패- 닉네임 중복)")
    void ChangeUserInfoWhenWhenDuplicatedNicknameTest(){
        User user = User.builder()
                .userid("userid")
                .password("test")
                .nickname("nick")
                .build();
        userRepository.save(user);
        User user1 = User.builder()
                .userid("userid1")
                .password("test1")
                .nickname("nick1")
                .build();
        userRepository.save(user1);
        //회원정보 변경
        RequestUser.changeInfo changeInfoDto = RequestUser.changeInfo.builder()
                .nickname("nick")
                .build();
        System.out.println("회원정보 수정 테스트(실패- 닉네임 중복)");
        assertThrows(RegisterFailedException.class,()->userService.changeUserInfo("userid1",changeInfoDto));
    }
    @Test
    @Transactional
    @DisplayName("회원정보 수정 테스트(성공)")
    void ChangeUserInfoTest(){
        User user = User.builder()
                .userid("userid")
                .password("password")
                .nickname("nick")
                .address("address")
                .build();
        userRepository.save(user);
        //회원정보 변경
        RequestUser.changeInfo changeInfoDto = RequestUser.changeInfo.builder()
                .password("changedpassword")
                .nickname("changednick")
                .address("changedaddress")
                .build();
        userService.changeUserInfo("userid",changeInfoDto);
        System.out.println("회원정보 수정 테스트(성공)");
        User user1 = userRepository.findByUserid("userid");
        System.out.println(user1.getPassword()+" "+user1.getNickname()+" "+user1.getAddress());
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
        likeRepository.save(like);
        Likes like1 = Likes.builder().board(board1).user(user).build();
        likeRepository.save(like1);
        //좋아요 리스트 출력
        System.out.println("좋아요 리스트");
        List<ResponseUser.MyLike> list = userService.listLikeBoard("userid");
        for(ResponseUser.MyLike myLike : list)
            System.out.println(myLike.getTitle());
    }

}
