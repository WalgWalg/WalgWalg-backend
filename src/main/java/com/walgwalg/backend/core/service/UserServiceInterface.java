package com.walgwalg.backend.core.service;

import com.walgwalg.backend.web.dto.RequestUser;

public interface UserServiceInterface {
    void register(RequestUser.register registerDto);
}
