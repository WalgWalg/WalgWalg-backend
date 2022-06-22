package com.walgwalg.backend.exception.errors;

import com.walgwalg.backend.exception.ErrorCode;

public class DuplicatedWalkException extends RuntimeException{
    public DuplicatedWalkException(){
        super(ErrorCode.WALK_DUPLICATED.getMessage());
    }
}
