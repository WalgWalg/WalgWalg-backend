package com.walgwalg.backend.exception.errors;

import com.walgwalg.backend.exception.ErrorCode;

public class NotFoundBoardException extends RuntimeException{
    public NotFoundBoardException (){
        super(ErrorCode.NOT_FOUND_BOARD.getMessage());
    }
}
