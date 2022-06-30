package com.walgwalg.backend.core.service;

import com.walgwalg.backend.web.dto.RequestUser;
import com.walgwalg.backend.web.dto.ResponseBoard;
import com.walgwalg.backend.web.dto.ResponseUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    void register(RequestUser.register registerDto);
    Optional<ResponseUser.login> login(RequestUser.login loginDto);
    void changeUserInfo(String userid, MultipartFile file, RequestUser.changeInfo changeInfoDto);
    Optional<ResponseUser.Token> updateAccessToken(String token);
    String createAccessToken(String userid);
    String createRefreshToken(String userid);
    ResponseUser.Info getUserInfo(String userid);
}
