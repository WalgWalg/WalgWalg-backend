package com.walgwalg.backend.core.service;

import com.walgwalg.backend.web.dto.RequestBoard;
import com.walgwalg.backend.web.dto.ResponseScrap;

import java.util.List;

public interface ScrapServiceInterface {
    void addScrap(String userid, String boardId);
    List<ResponseScrap.MyScrap> getScrap(String userid);
    void deleteScrap(String userid, String boardId);
}
