package com.walgwalg.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_001", " AUTHENTICATION_FAILED."),
    LOGIN_FAILED(HttpStatus.NOT_FOUND, "AUTH_002", " LOGIN_FAILED."),
    AUTHENTICATION_CONFLICT(HttpStatus.CONFLICT,"AUTH__009"," AUTHENTICATION_CONFLICT."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,"USER_001","NOT_FOUND_USER"),
    NOT_FOUND_WALK(HttpStatus.NOT_FOUND,"WALK_001","NOT_FOUND_WALK"),
    WALK_DUPLICATED(HttpStatus.FORBIDDEN, "WALK_002","WALK_DUPLICATED"),
    NOT_FOUND_BOARD(HttpStatus.NOT_FOUND,"BOARD_001","NOT_FOUND_BOARD"),
    LIKE_DUPLICATED(HttpStatus.FORBIDDEN,"LIKE_001", "LIKE_DUPLICATED"),
    NOT_FOUND_LIKE(HttpStatus.NOT_FOUND, "LIKE_002", "NOT_FOUND_LIKE"),
    SCRAP_DUPLICATED(HttpStatus.FORBIDDEN,"SCRAP_001", "SCRAP_DUPLICATED"),
    NOT_FOUND_SCRAP(HttpStatus.NOT_FOUND, "SCRAP_002", "NOT_FOUND_SCRAP");
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(final HttpStatus httpStatus, final String code, final String message){
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
