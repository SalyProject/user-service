package com.saly.user.common.exception;

public class SallyException extends HasError {
    public SallyException(ApiErrorCode errorCode) {
        super(errorCode, null, null);
    }

    public SallyException(ApiErrorCode errorCode, String debugInfo) {
        super(errorCode, debugInfo, null);
    }
}
