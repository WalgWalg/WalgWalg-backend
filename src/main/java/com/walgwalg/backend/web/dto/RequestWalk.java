package com.walgwalg.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class RequestWalk {

    @Builder
    @Data
    public static class start{
        @NotEmpty(message = "날짜가 비어있음")
        private Date walkDate;
        @NotEmpty(message = "공원 이름이 비어있음")
        private String location;
    }

    @Builder
    @Data
    public static class addGps{
        @NotEmpty(message = "날짜가 비어있음")
        private Date walkDate;
        @NotEmpty(message = "위도가 비어있음")
        private double latitude;
        @NotEmpty(message = "경도가 비어있음")
        private double longitude;
    }
}
