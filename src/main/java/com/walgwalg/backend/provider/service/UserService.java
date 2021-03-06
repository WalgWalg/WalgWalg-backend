package com.walgwalg.backend.provider.service;

import com.walgwalg.backend.core.security.role.Role;
import com.walgwalg.backend.core.service.UserServiceInterface;
import com.walgwalg.backend.entity.Users;
import com.walgwalg.backend.exception.errors.CustomJwtRuntimeException;
import com.walgwalg.backend.exception.errors.LoginFailedException;
import com.walgwalg.backend.exception.errors.NotFoundUserException;
import com.walgwalg.backend.exception.errors.RegisterFailedException;
import com.walgwalg.backend.provider.security.JwtAuthToken;
import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import com.walgwalg.backend.repository.UsersRepository;
import com.walgwalg.backend.util.SHA256Util;
import com.walgwalg.backend.web.dto.RequestUser;
import com.walgwalg.backend.web.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInterface {
    private final UsersRepository usersRepository;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;
    private final S3Service s3Service;

    @Transactional
    @Override
    public void register(RequestUser.register registerDto){
        //아이디 중복 확인
        Users user = usersRepository.findByUserid(registerDto.getUserid());
        if(user != null){ //아이디 중복
            throw new RegisterFailedException();
        }
        //닉네임 중복 확인
        user = usersRepository.findByNickname(registerDto.getNickname());
        if(user != null){//닉네임 중복
            throw new RegisterFailedException();
        }
        //salt 생성
        String salt = SHA256Util.generateSalt();
        //salt랑 비밀번호 암호화
        String encryptedPassword = SHA256Util.getEncrypt(registerDto.getPassword(),salt);

        user = Users.builder()
                .userid(registerDto.getUserid())
                .password(encryptedPassword)
                .nickname(registerDto.getNickname())
                .address(registerDto.getAddress())
                .salt(salt)
                .build();
        usersRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<ResponseUser.login> login(RequestUser.login loginDto){
        //회원이 존재하는지
        Users user = usersRepository.findByUserid(loginDto.getUserid());
        if(user == null){//회원이 존재하지 않을 경우
            throw new LoginFailedException();
        }
        String salt = user.getSalt();
        //아이디가 있으면 거기 솔트 꺼내서 패스워드 암호화한다음 db에서 찾아보기
        user = usersRepository.findByUseridAndPassword(loginDto.getUserid(),SHA256Util.getEncrypt(loginDto.getPassword(),salt));
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
    public void changeUserInfo(String userid, MultipartFile file, RequestUser.changeInfo changeInfoDto){
        Users user = usersRepository.findByUserid(userid);
        if(user == null){//유저 정보가 없을 경우
            throw new NotFoundUserException();
        }
        Users user1 = usersRepository.findByNickname(changeInfoDto.getNickname());
        if(user1 != null && !user.equals(user1)){//닉네임 중복
            throw new RegisterFailedException();
        }

        if(!Optional.ofNullable(user.getProfile()).isEmpty()){//프로필 사진이 이미 s3에 등록 됐을 경우 삭제 후 업로드
            s3Service.deleteFile(user.getProfile());
        }
        //프로필 사진 s3에 등록
        String url="";
        try {
            url = s3Service.upload(file, "user");
        }catch (IOException e){
            System.out.println("s3 등록 실패");
        }

        //salt생성
        String salt = SHA256Util.generateSalt();
        //비밀번호 암호화
        String encryptedPassword = SHA256Util.getEncrypt(changeInfoDto.getPassword(),salt);
        //유저 정보 수정
        user.changeUserInfo(encryptedPassword,changeInfoDto.getNickname(),changeInfoDto.getAddress(),salt,url);
    }

    @Transactional
    @Override
    public ResponseUser.Info getUserInfo(String userid){
        Users user = usersRepository.findByUserid(userid);
        if(user == null){//유저 정보가 없을 경우
            throw new NotFoundUserException();
        }
        ResponseUser.Info response = ResponseUser.Info.builder()
                .nickname(user.getNickname())
                .profile(user.getProfile())
                .address(user.getAddress())
                .build();
        return response;
    }

    @Transactional
    @Override
    public Optional<ResponseUser.Token> updateAccessToken(String token){
        if(token == null || token.equals("null")){
            throw new CustomJwtRuntimeException();
        }
        Users user = usersRepository.findByRefreshToken(token);
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
