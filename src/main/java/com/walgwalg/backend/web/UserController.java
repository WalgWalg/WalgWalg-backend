package com.walgwalg.backend.web;

import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.exception.errors.LoginFailedException;
import com.walgwalg.backend.exception.errors.RegisterFailedException;
import com.walgwalg.backend.provider.security.JwtAuthToken;
import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import com.walgwalg.backend.provider.service.UserService;
import com.walgwalg.backend.web.dto.RequestUser;
import com.walgwalg.backend.web.dto.ResponseMessage;
import com.walgwalg.backend.web.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtAuthTokenProvider jwtAuthTokenProvider;

    @PostMapping("/user/register")
    public ResponseEntity<ResponseMessage> userRegister(@Valid @RequestBody RequestUser.register registerDto) {
        userService.register(registerDto);
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("회원가입 성공")
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/user/login")
    public ResponseEntity<ResponseMessage> userLogin(@Valid @RequestBody RequestUser.login loginDto){
        ResponseUser.login user = userService.login(loginDto).orElseThrow(()-> new LoginFailedException());

        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("로그인 성공")
                .list(user)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/user/changeUserInfo")
    public ResponseEntity<ResponseMessage> changeUserInfo(HttpServletRequest request, @RequestBody RequestUser.changeInfo changeInfoDto){
        Optional<String> token = jwtAuthTokenProvider.getAuthToken(request);
        String userid = null;
        if(token.isPresent()){
            JwtAuthToken jwtAuthToken = jwtAuthTokenProvider.convertAuthToken(token.get());
            userid = jwtAuthToken.getClaims().getSubject();
        }
        userService.changeUserInfo(userid, changeInfoDto);
        ResponseMessage response = ResponseMessage.builder()
                .status(HttpStatus.OK.value())
                .message("회원정보 수정 성공")
                .build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
