package com.walgwalg.backend.exception.errors;

import com.walgwalg.backend.exception.ErrorCode;

public class DuplicatedParkException extends RuntimeException{
    public DuplicatedParkException(){
        super(ErrorCode.LIKE_DUPLICATED.getMessage());
    }
}
