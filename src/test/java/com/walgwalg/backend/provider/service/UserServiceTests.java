package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.exception.errors.LoginFailedException;
import com.walgwalg.backend.exception.errors.RegisterFailedException;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.web.dto.RequestUser;
import com.walgwalg.backend.web.dto.ResponseUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

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
}
