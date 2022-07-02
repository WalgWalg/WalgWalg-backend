package com.walgwalg.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RequestUser {
    @Builder
    @Data
    public static class register{
        @NotNull(message = "아이디가 비어있음")
        private String userid;
        @NotNull(message = "비밀번호가 비어있음")
        private String password;
        @NotNull(message = "닉네임이 비어있음")
        private String nickname;
        @NotNull(message = "주소가 비어있음")
        private String address;
    }
    @Builder
    @Data
    public static class login{
        @NotNull(message = "아이디가 비어있음")
        private String userid;
        @NotNull(message = "비밀번호가 비어있음")
        private String password;
    }

    @Builder
    @Data
    public static class changeInfo{
        private String password;
        private String nickname;
        private String address;
    }
}
