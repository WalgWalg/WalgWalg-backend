package com.walgwalg.backend.exception.errors;

import com.walgwalg.backend.exception.ErrorCode;

public class NotFoundLikesException extends RuntimeException{
    public NotFoundLikesException(){
        super(ErrorCode.NOT_FOUND_LIKE.getMessage());
    }
}
