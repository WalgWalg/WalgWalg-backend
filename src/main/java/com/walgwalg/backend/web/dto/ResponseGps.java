package com.walgwalg.backend.web.dto;

import lombok.Builder;
import lombok.Data;

public class ResponseGps {
    @Data
    @Builder
    public static class gps{
        private double latitude;
        private double longitude;
    }
}
