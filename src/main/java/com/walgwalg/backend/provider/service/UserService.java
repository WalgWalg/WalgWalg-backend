package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.core.security.role.Role;
import com.walgwalg.backend.core.service.UserServiceInterface;
import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.Likes;
import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.exception.errors.CustomJwtRuntimeException;
import com.walgwalg.backend.exception.errors.LoginFailedException;
import com.walgwalg.backend.exception.errors.NotFoundUserException;
import com.walgwalg.backend.exception.errors.RegisterFailedException;
import com.walgwalg.backend.provider.security.JwtAuthToken;
import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import com.walgwalg.backend.repository.BoardRepository;
import com.walgwalg.backend.repository.LikesRepository;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.util.SHA256Util;
import com.walgwalg.backend.web.dto.RequestUser;
import com.walgwalg.backend.web.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Override
    @Transactional
    public Optional<ResponseUser.login> login(RequestUser.login loginDto){
        //회원이 존재하는지
        User user = userRepository.findByUserid(loginDto.getUserid());
        if(user == null){//회원이 존재하지 않을 경우
            throw new LoginFailedException();
        }
        String salt = user.getSalt();
        //아이디가 있으면 거기 솔트 꺼내서 패스워드 암호화한다음 db에서 찾아보기
        user = userRepository.findByUseridAndPassword(loginDto.getUserid(),SHA256Util.getEncrypt(loginDto.getPassword(),salt));
        if(user == null){//비밀번호가 맞지 않을 경우
            throw new LoginFailedException();
        }
        //로그인 성공
        String refreshToken = createRefreshToken(user.getUserid());//refreshToken 갱신
        ResponseUser.login login = ResponseUser.login.builder()
                .accessToken(createAccessToken(user.getUserid()))
                .refreshToken(refreshToken)
                .build();
        //로그인 때마다 refreshToken 업데이트
        user.changeRefreshToken(refreshToken);

        return Optional.ofNullable(login);
    }
    @Override
    @Transactional
    public void changeUserInfo(String userid, RequestUser.changeInfo changeInfoDto){
        User user = userRepository.findByUserid(userid);
        if(user == null){//유저 정보가 없을 경우
            throw new NotFoundUserException();
        }
        User user1 =userRepository.findByNickname(changeInfoDto.getNickname());
        if(user1 != null && !user.equals(user1)){//닉네임 중복
            throw new RegisterFailedException();
        }
        //salt생성
        String salt = SHA256Util.generateSalt();
        //비밀번호 암호화
        String encryptedPassword = SHA256Util.getEncrypt(changeInfoDto.getPassword(),salt);
        //유저 정보 수정
        user.changeUserInfo(encryptedPassword,changeInfoDto.getNickname(),changeInfoDto.getAddress(),salt);
    }
    @Transactional
    @Override
    public Optional<ResponseUser.Token> updateAccessToken(String token){
        if(token == null || token.equals("null")){
            throw new CustomJwtRuntimeException();
        }
        User user = userRepository.findByRefreshToken(token);
        if(user == null){
            throw new NotFoundUserException();
        }
        if(!user.getRefreshToken().equals(token)){
            throw new CustomJwtRuntimeException();
        }
        //토큰 유효성 검증
        JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token);
        if(!jwtAuthToken.validate() || !jwtAuthToken.getClaims().get("role").equals(Role.USER.getCode())){
         return Optional.empty();
        }
        String id = String.valueOf(jwtAuthToken.getClaims().getSubject());
        String accessToken = createAccessToken(id); //accessToken 재발급

        ResponseUser.Token newToken = ResponseUser.Token.builder()
                .accessToken(accessToken)
                .refreshToken(token)
                .build();
        return Optional.ofNullable(newToken);
    }
    @Override
    public String createAccessToken(String userid) {
        //유효기간 설정-2분
        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(50).atZone(ZoneId.systemDefault()).toInstant());
        //토큰 발급
        JwtAuthToken accessToken = jwtAuthTokenProvider.createAuthToken(userid, Role.USER.getCode(),expiredDate);
        return accessToken.getToken();
    }
    @Override
    public String createRefreshToken(String userid) {
        //유효기간 설정-1년
        Date expiredDate = Date.from(LocalDateTime.now().plusYears(1) .atZone(ZoneId.systemDefault()).toInstant());
        //토큰 발급
        JwtAuthToken refreshToken = jwtAuthTokenProvider.createAuthToken(userid, Role.USER.getCode(),expiredDate);
        return refreshToken.getToken();
    }
}
