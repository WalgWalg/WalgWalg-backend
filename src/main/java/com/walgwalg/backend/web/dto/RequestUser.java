package com.walgwalg.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

public class RequestUser {
    @Builder
    @Data
    public static class register{
        @NotEmpty(message = "아이디가 비어있음")
        private String userid;
        @NotEmpty(message = "비밀번호가 비어있음")
        private String password;
        @NotEmpty(message = "닉네임이 비어있음")
        private String nickname;
        @NotEmpty(message = "주소가 비어있음")
        private String address;
    }
    @Builder
    @Data
    public static class login{
        @NotEmpty(message = "아이디가 비어있음")
        private String userid;
        @NotEmpty(message = "비밀번호가 비어있음")
        private String password;
    }

    @Builder
    @Data
    public static class changeInfo{
        private String password;
        private String nickname;
        private String address;
        private String profile_path;
    }
}
