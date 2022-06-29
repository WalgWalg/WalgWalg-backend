package com.walgwalg.backend.web.dto;

import com.walgwalg.backend.entity.Park;
import lombok.Builder;
import lombok.Data;

public class ResponsePark {
    @Data
    @Builder
    public static class getAll{
        private String parkName;
        private String roadAddress;
        private String numberAddress;
        private String latitude; //위도
        private String longitude; //경도
    }
}
