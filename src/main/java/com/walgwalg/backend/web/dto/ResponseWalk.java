package com.walgwalg.backend.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;

public class ResponseWalk {
    @Builder
    @Data
    public static class list {
        private String id;
        private Date walkDate;
        private Long stepCount;
        private Long distance;
        private Long calorie;
        private String walkTime;
        private String course;
        private String location;
    }
    @Builder
    @Data
    public static class calendar{
        private Long stepCount; //걸음수
        private Long distance;
        private String walkTime;

    }

    @Builder
    @Data
    public static class total{
        public Long stepCount; //걸음수
        private Long distance;
        private String walkTime;
    }

    @Builder
    @Data
    public static class mainInfo{
        public String nickName;
        public Map<String, ResponseWalk.total> walkTotal;
    }
}
