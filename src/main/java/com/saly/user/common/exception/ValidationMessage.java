package com.saly.user.common.exception;

import org.springframework.http.HttpStatus;

public interface ValidationMessage extends ApiErrorCode {

    @Override
    default String getTitle() {
        return ErrorCode.BAD_REQUEST.getTitle();
    }

    @Override
    default int getCode() {
        return ErrorCode.BAD_REQUEST.getCode();
    }

    @Override
    default HttpStatus getHttpStatus() {
        return ErrorCode.BAD_REQUEST.getHttpStatus();
    }

    default BadRequestException ofException(){
        return new BadRequestException(this);
    }

    default BadRequestException ofException(String debugInfo){
        return new BadRequestException(this, debugInfo);
    }
}
