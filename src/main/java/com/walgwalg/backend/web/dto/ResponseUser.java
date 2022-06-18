package com.walgwalg.backend.web.dto;

import com.walgwalg.backend.entity.Board;
import com.walgwalg.backend.entity.HashTag;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class ResponseUser {
    @Data
    @Builder
    public static class login{
        private String accessToken;
        private String refreshToken;
    }
    @Data
    @Builder
    public static class Token{
        private String accessToken;
        private String refreshToken;
    }
}
