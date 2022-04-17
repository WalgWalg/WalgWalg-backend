package com.walgwalg.backend.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
public class ResponseMessage {
    @Builder.Default // 필드 특정 값으로 초기화
    private String id = UUID.randomUUID().toString(); //유일한 키 생성
    @Builder.Default
    private Date dateTime = new Date();
    private int status;
    private String message;
    private Object list;
}
