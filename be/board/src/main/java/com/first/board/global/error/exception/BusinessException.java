package com.first.board.global.error.exception;

import com.first.board.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public BusinessException(ErrorCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}

