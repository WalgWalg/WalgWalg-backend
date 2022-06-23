package com.walgwalg.backend.exception.errors;

import com.walgwalg.backend.exception.ErrorCode;

public class NotFoundScrapException extends RuntimeException{
    public NotFoundScrapException(){
        super(ErrorCode.NOT_FOUND_SCRAP.getMessage());
    }
}
