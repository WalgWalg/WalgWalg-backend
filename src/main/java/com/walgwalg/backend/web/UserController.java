package com.walgwalg.backend.web;

import com.walgwalg.backend.entity.User;
import com.walgwalg.backend.exception.errors.LoginFailedException;
import com.walgwalg.backend.exception.errors.RegisterFailedException;
import com.walgwalg.backend.provider.security.JwtAuthTokenProvider;
import com.walgwalg.backend.provider.service.UserService;
import com.walgwalg.backend.repository.UserRepository;
import com.walgwalg.backend.web.dto.RequestUser;
import com.walgwalg.backend.web.dto.ResponseMessage;
import com.walgwalg.backend.web.dto.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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
}
