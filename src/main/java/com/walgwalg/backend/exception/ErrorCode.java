package com.walgwalg.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_001", " AUTHENTICATION_FAILED."),
    LOGIN_FAILED(HttpStatus.NOT_FOUND, "AUTH_002", " LOGIN_FAILED."),
    AUTHENTICATION_CONFLICT(HttpStatus.CONFLICT,"AUTH__009"," AUTHENTICATION_CONFLICT."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"USER__001","NOT_FOUND_USER");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus httpStatus, final String code, final String message){
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
