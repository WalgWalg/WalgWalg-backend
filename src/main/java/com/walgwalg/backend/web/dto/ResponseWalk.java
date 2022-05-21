package com.walgwalg.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

public class ResponseWalk {
    @Builder
    @Data
    public static class list {
        private Long id;
        private Date walkDate;
        private Integer step_count;
        private float distance;
        private Integer calorie;
        private Integer walkTime;
        private String course;
        private String location;
    }
}
