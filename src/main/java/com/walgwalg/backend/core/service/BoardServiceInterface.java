package com.walgwalg.backend.core.service;

import com.walgwalg.backend.web.dto.RequestBoard;
import com.walgwalg.backend.web.dto.ResponseBoard;
import com.walgwalg.backend.web.dto.ResponseUser;

import java.util.List;

public interface BoardServiceInterface {
    void addLike(String userid, Long boardId);
    void addScrap(String userid, RequestBoard.scrap requestDto);
    List<ResponseBoard.MyLike> listLikeBoard(String userid);
    void deleteLikeBoard(Long boardId);
    void registerBoard(String userid, RequestBoard.register requestDto);
}
