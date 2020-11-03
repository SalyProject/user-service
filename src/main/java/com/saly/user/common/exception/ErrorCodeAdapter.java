package com.saly.user.common.exception;

import org.springframework.http.HttpStatus;

public class ErrorCodeAdapter implements ApiErrorCode {
    private ErrorCode errorCode;

    public ErrorCodeAdapter(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.errorCode.getHttpStatus();
    }

    @Override
    public String getTitle() {
        return this.errorCode.getTitle();
    }

    @Override
    public String getMessage() {
        return this.errorCode.getMessage();
    }

    @Override
    public int getCode() {
        return this.errorCode.getCode();
    }
}
