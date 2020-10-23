package com.saly.user;

import com.saly.user.common.exception.ApiErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {
    private int errorCode;
    private String title;
    private String message;

    public Error(ApiErrorCode error) {
        this.errorCode = error.getCode();
        this.title = error.getTitle();
        this.message = error.getMessage();
    }
}
