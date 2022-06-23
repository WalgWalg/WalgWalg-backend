package com.walgwalg.backend.core.service;

import com.walgwalg.backend.web.dto.ResponseBoard;
import com.walgwalg.backend.web.dto.ResponseLike;

import java.util.List;

public interface LikeServiceInterface {
    void addLike(String userid, String boardId);
    List<ResponseLike.MyLike> listLikeBoard(String userid);
    void deleteLikeBoard(String userid,String boardId);

}
