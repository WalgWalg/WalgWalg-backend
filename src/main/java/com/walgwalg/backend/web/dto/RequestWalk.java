package com.walgwalg.backend.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
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

    @Builder
    @Data
    public static class registerWalk{
        @NotEmpty(message = "날짜가 비어있음")
        private Date walkDate;
        @NotEmpty(message = "걸음수가 비어있음")
        private Integer step_count;
        @NotEmpty(message = "거리가 비어있음")
        private float distance;
        @NotEmpty(message = "칼로리가 비어있음")
        private Integer calorie;
        @NotEmpty(message = "소요시간이 비어있음")
        private Integer walkTime;
    }
}
