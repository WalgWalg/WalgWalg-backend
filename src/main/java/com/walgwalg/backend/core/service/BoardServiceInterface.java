package com.walgwalg.backend.core.service;

import com.walgwalg.backend.web.dto.RequestBoard;
import com.walgwalg.backend.web.dto.ResponseBoard;
import com.walgwalg.backend.web.dto.ResponseUser;

import java.util.List;

public interface BoardServiceInterface {
    void registerBoard(String userid, RequestBoard.register requestDto);
    ResponseBoard.getBoard getBoard(String boardId);
    List<ResponseBoard.list> getMyBoard(String userId);
    void deleteBoard(String userId, String boardId);
    List<ResponseBoard.top> getBoardTop();
}
