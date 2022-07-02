package com.walgwalg.backend.exception;

import com.walgwalg.backend.exception.errors.*;
import com.walgwalg.backend.web.dto.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomJwtRuntimeException.class)
    protected ResponseEntity<ResponseMessage> handleCustomJwtRuntimeException(CustomJwtRuntimeException e) {
        ErrorCode code = ErrorCode.AUTHENTICATION_FAILED;

        ResponseMessage response = ResponseMessage.builder()
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .list(code.getCode())
                .build();
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
    @ExceptionHandler(DuplicatedLikeException.class)
    protected ResponseEntity<ResponseMessage> handleDuplicatedLikeException(DuplicatedLikeException e) {
        ErrorCode code = ErrorCode.LIKE_DUPLICATED;

        ResponseMessage response = ResponseMessage.builder()
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .list(code.getCode())
                .build();
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
    @ExceptionHandler(DuplicatedScrapException.class)
    protected ResponseEntity<ResponseMessage> handleDuplicatedScrapException(DuplicatedScrapException e) {
        ErrorCode code = ErrorCode.SCRAP_DUPLICATED;

        ResponseMessage response = ResponseMessage.builder()
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .list(code.getCode())
                .build();
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
    @ExceptionHandler(LoginFailedException.class)
    protected ResponseEntity<ResponseMessage> handleLoginFailedException(LoginFailedException e) {
        ErrorCode code = ErrorCode.LOGIN_FAILED;

        ResponseMessage response = ResponseMessage.builder()
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .list(code.getCode())
                .build();
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
    @ExceptionHandler(NotFoundBoardException.class)
    protected ResponseEntity<ResponseMessage> handleNotFoundBoardException(NotFoundBoardException e) {
        ErrorCode code = ErrorCode.NOT_FOUND_BOARD;

        ResponseMessage response = ResponseMessage.builder()
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .list(code.getCode())
                .build();
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
    @ExceptionHandler(NotFoundUserException.class)
    protected ResponseEntity<ResponseMessage> handleNotFoundUserException(NotFoundUserException e) {
        ErrorCode code = ErrorCode.NOT_FOUND_USER;

        ResponseMessage response = ResponseMessage.builder()
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .list(code.getCode())
                .build();
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
    @ExceptionHandler(NotFoundWalkException.class)
    protected ResponseEntity<ResponseMessage> handleNotFoundWalkException(NotFoundWalkException e) {
        ErrorCode code = ErrorCode.NOT_FOUND_WALK;

        ResponseMessage response = ResponseMessage.builder()
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .list(code.getCode())
                .build();
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
    @ExceptionHandler(RegisterFailedException.class)
    protected ResponseEntity<ResponseMessage> handleRegisterFailedException(RegisterFailedException e) {
        ErrorCode code = ErrorCode.AUTHENTICATION_CONFLICT;

        ResponseMessage response = ResponseMessage.builder()
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .list(code.getCode())
                .build();
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
    @ExceptionHandler(DuplicatedWalkException.class)
    protected ResponseEntity<ResponseMessage> handleDuplicatedWalkException(DuplicatedWalkException e) {
        ErrorCode code = ErrorCode.WALK_DUPLICATED;

        ResponseMessage response = ResponseMessage.builder()
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .list(code.getCode())
                .build();
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
    @ExceptionHandler(NotFoundLikesException.class)
    protected ResponseEntity<ResponseMessage> handleNotFoundLikesException(NotFoundLikesException e) {
        ErrorCode code = ErrorCode.NOT_FOUND_LIKE;

        ResponseMessage response = ResponseMessage.builder()
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .list(code.getCode())
                .build();
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
    @ExceptionHandler(NotFoundScrapException.class)
    protected ResponseEntity<ResponseMessage> handleNotFoundScrapException(NotFoundScrapException e) {
        ErrorCode code = ErrorCode.NOT_FOUND_SCRAP;

        ResponseMessage response = ResponseMessage.builder()
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .list(code.getCode())
                .build();
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
    @ExceptionHandler(DuplicatedParkException.class)
    protected ResponseEntity<ResponseMessage> handleDuplicatedParkException(DuplicatedParkException e) {
        ErrorCode code = ErrorCode.PARK_DUPLICATED;

        ResponseMessage response = ResponseMessage.builder()
                .status(code.getHttpStatus().value())
                .message(code.getMessage())
                .list(code.getCode())
                .build();
        return new ResponseEntity<>(response, code.getHttpStatus());
    }
}
