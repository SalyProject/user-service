package com.saly.user.common.exception;

import lombok.Getter;

public class BadRequestException extends SallyException {

    public BadRequestException(String message) {
        super(new BadRequestErrorCode(message));
    }

    @Getter
    public static class BadRequestErrorCode extends ErrorCodeAdapter {
        private final String message;

        public BadRequestErrorCode(String message) {
            super(ErrorCode.BAD_REQUEST);
            this.message = message;
        }
    }
}
