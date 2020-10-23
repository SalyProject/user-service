package com.saly.user.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements ApiErrorCode {
    INTERNAL_ERROR(101, "Internal error", "Something wrong in internal service logic.", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(102, "Bad Request parameter", "Request parameters are invalid or missing.", HttpStatus.BAD_REQUEST),
    EMPTY_REQUEST_BODY(103, "Request body is empty", "Request body is missing.", HttpStatus.BAD_REQUEST);

    private int code;
    private String title;
    private String message;
    private HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    ErrorCode(int code, String title, String message, HttpStatus httpStatus) {
        this(code, message, httpStatus);
        this.title = title;
    }

}
