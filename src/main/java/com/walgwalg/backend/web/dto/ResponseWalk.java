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
        private Integer stepCount;
        private Integer distance;
        private Integer calorie;
        private String walkTime;
        private String course;
        private String location;
    }
    @Builder
    @Data
    public static class calendar{
        private Integer stepCount; //걸음수
        private Integer distance;
        private String walkTime;

    }

    @Builder
    @Data
    public static class total{
        public Integer stepCount; //걸음수
        private Integer distance;
        private String walkTime;
    }

    @Builder
    @Data
    public static class mainInfo{
        public String nickName;
        public Map<String, ResponseWalk.total> walkTotal;
    }
}
