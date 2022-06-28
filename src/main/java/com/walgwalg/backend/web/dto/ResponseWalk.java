package com.walgwalg.backend.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

public class ResponseWalk {
    @Builder
    @Data
    public static class list {
        private String id;
        private Date walkDate;
        private Integer stepCount;
        private float distance;
        private Integer calorie;
        private Date walkTime;
        private String course;
        private String location;
    }
    @Builder
    @Data
    public static class calendar{
        private Integer stepCount; //걸음수
        private float distance;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private Date walkTime;

    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class total{
        public Integer stepCount; //걸음수
        private float distance;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private Date walkTime;
    }
}
