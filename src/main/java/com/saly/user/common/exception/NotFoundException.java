package com.saly.user.common.exception;

import lombok.Getter;

public class NotFoundException extends SallyException {

    public NotFoundException(String message) {
        super(new NotFoundErrorCode(message));
    }

    @Getter
    public static class NotFoundErrorCode extends ErrorCodeAdapter {

        private final String message;

        public NotFoundErrorCode(String message) {
            super(ErrorCode.NOT_FOUND);
            this.message = message;
        }
    }
}
