package com.walgwalg.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParkInfo {
    private String parkName;
    private String roadAddress;
    private String numberAddress;
    private String latitude; //위도
    private String longitude; //경도
}
