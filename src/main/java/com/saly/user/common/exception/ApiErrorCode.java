package com.saly.user.common.exception;

import org.springframework.http.HttpStatus;

public interface ApiErrorCode {
    HttpStatus getHttpStatus();

    String getTitle();

    String getMessage();

    default String getDetails() {
        return "";
    }

    int getCode();

}
