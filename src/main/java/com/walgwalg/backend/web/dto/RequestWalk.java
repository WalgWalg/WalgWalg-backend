package com.walgwalg.backend.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class RequestWalk {

    @Builder
    @Data
    public static class start{
        @NotNull(message = "날짜가 비어있음")
        private Date walkDate;
        @NotNull(message = "공원 이름이 비어있음")
        private String location;
        @NotNull(message = "상세 주소가 비어있음")
        private String address;
    }

    @Builder
    @Data
    public static class addGps{
        @NotNull(message = "산책번호가 비어있음")
        private String walkId;
        @NotNull(message = "위도가 비어있음")
        private String latitude;
        @NotNull(message = "경도가 비어있음")
        private String longitude;
    }

    @Builder
    @Data
    public static class registerWalk{
        @NotNull(message = "산책번호가 비어있음")
        private String walkId;
        @NotNull(message = "걸음수가 비어있음")
        private Integer step_count;
        @NotNull(message = "거리가 비어있음")
        private Integer distance;
        @NotNull(message = "칼로리가 비어있음")
        private Integer calorie;
        @NotNull(message = "산책시간이 비어있음")
        private String walkTime;
    }

}
