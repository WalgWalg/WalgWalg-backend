package com.walgwalg.backend.exception.errors;

import com.walgwalg.backend.exception.ErrorCode;

public class DuplicatedScrapException extends RuntimeException{
    public DuplicatedScrapException(){
        super(ErrorCode.SCRAP_DUPLICATED.getMessage());
    }
}
