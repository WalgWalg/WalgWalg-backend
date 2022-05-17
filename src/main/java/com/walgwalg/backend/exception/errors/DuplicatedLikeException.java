package com.walgwalg.backend.exception.errors;

import com.walgwalg.backend.exception.ErrorCode;

public class DuplicatedLikeException extends RuntimeException{
    public DuplicatedLikeException(){
        super(ErrorCode.LIKE_DUPLICATED.getMessage());
    }
}
