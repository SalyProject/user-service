package com.saly.user.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode implements ApiErrorCode {
    // 101-199 - internal cases
    INTERNAL_ERROR(101, "Internal error", "Something wrong in internal service logic.", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(102, "Bad Request parameter", "Request parameters are invalid or missing.", HttpStatus.BAD_REQUEST),
    EMPTY_REQUEST_BODY(103, "Request body is empty", "Request body is missing.", HttpStatus.BAD_REQUEST),
    NOT_FOUND(104, "Resource not found", "Request resource is not found.", HttpStatus.NOT_FOUND),

    // auth
    ACCESS_DENIED(201, "Access denied", "Access denied for this user", HttpStatus.FORBIDDEN),
    USER_NOT_ACTIVE(202, "User is not active", "User should activate account", HttpStatus.FORBIDDEN);

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
