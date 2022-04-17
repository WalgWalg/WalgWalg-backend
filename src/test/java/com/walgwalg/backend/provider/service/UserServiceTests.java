package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.exception.errors.RegisterFailedException;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.web.dto.RequestUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
}
