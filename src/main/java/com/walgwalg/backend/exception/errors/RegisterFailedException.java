package com.walgwalg.backend.exception.errors;

import com.walgwalg.backend.exception.ErrorCode;

public class RegisterFailedException extends RuntimeException{
    public RegisterFailedException(){
        super(ErrorCode.AUTHENTICATION_CONFLICT.getMessage());
    }
}
