package com.saly.user.common.exception;

import static java.util.Optional.ofNullable;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class HasError extends RuntimeException {
    private ApiErrorCode errorCode;
    private String errorMessage;
    private final String debugInfo;
    private boolean printStackTrace;

    protected HasError(ApiErrorCode errorCode, String debugInfo, Throwable cause) {
        super(buildMessage(errorCode, debugInfo, cause), cause);

        this.errorCode = errorCode;
        this.errorMessage = ofNullable(cause).map(Throwable::getMessage).orElse(errorCode.getMessage());
        this.debugInfo = debugInfo;
        this.printStackTrace = Objects.nonNull(cause);
    }

    private static String buildMessage(ApiErrorCode errorCode, String debugInfo, Throwable cause) {
        debugInfo = ofNullable(debugInfo).filter(info -> !info.isBlank()).orElse("no-debug-info");
        String errorMessage = ofNullable(cause).map(Throwable::getMessage).orElse(errorCode.getMessage());

        return "Error [" + errorCode + "] " + debugInfo + " | " + errorMessage;
    }


}
