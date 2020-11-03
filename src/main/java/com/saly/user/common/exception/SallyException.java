package com.saly.user.common.exception;

import lombok.Getter;

public class SallyException extends HasError {
    public SallyException(ApiErrorCode errorCode) {
        super(errorCode, null, null);
    }

    public SallyException(String message) {
        this(new SallyErrorCode(message));
    }

    @Getter
    static class SallyErrorCode extends ErrorCodeAdapter {
        private String message;

        public SallyErrorCode(final String message) {
            super(ErrorCode.INTERNAL_ERROR);
            this.message = message;
        }
    }
}
