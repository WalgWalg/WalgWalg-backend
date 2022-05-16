package com.walgwalg.backend.exception.errors;

import com.walgwalg.backend.exception.ErrorCode;

public class NotFoundWalkException extends RuntimeException{
    public NotFoundWalkException(){
        super(ErrorCode.NOT_FOUND_WALK.getMessage());
    }
}
