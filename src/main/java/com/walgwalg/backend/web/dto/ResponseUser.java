package com.walgwalg.backend.web.dto;

import lombok.Builder;
import lombok.Data;

public class ResponseUser {
    @Data
    @Builder
    public static class login{
        private String accessToken;
        private String refreshToken;
    }
}
