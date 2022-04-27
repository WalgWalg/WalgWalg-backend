package com.walgwalg.backend.core.service;

import com.walgwalg.backend.web.dto.RequestBoard;

public interface BoardServiceInterface {
    void addLike(String userid, RequestBoard.like requestDto);
}
