package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.core.service.UserServiceInterface;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.exception.errors.RegisterFailedException;
import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.util.SHA256Util;
import com.walgwalg.backend.web.dto.RequestUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UserRepository userRepository;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @Transactional
    @Override
    public void register(RequestUser.register registerDto){
        //아이디 중복 확인
        User user = userRepository.findByUserid(registerDto.getUserid());
        if(user != null){ //아이디 중복
            throw new RegisterFailedException();
        }
        //닉네임 중복 확인
        user = userRepository.findByNickname(registerDto.getNickname());
        if(user != null){//닉네임 중복
            throw new RegisterFailedException();
        }
        //salt 생성
        String salt = SHA256Util.generateSalt();
        //salt랑 비밀번호 암호화
        String encryptedPassword = SHA256Util.getEncrypt(registerDto.getPassword(),salt);

        user = User.builder()
                .userid(registerDto.getUserid())
                .password(encryptedPassword)
                .nickname(registerDto.getNickname())
                .address(registerDto.getAddress())
                .salt(salt)
                .build();
        userRepository.save(user);
    }
}
