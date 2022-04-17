package com.walgwalg.backend.core.service;

import com.walgwalg.backend.web.dto.RequestUser;
import com.walgwalg.backend.web.dto.ResponseUser;

import java.util.Optional;

public interface UserServiceInterface {
    void register(RequestUser.register registerDto);
    Optional<ResponseUser.login> login(RequestUser.login loginDto);
    String createAccessToken(String userid);
    String createRefreshToken(String userid);
}
