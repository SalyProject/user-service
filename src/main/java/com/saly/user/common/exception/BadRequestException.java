package com.saly.user.common.exception;

public class BadRequestException extends SallyException {

    public BadRequestException(ValidationMessage errorCode) {
        super(errorCode);
    }

    public BadRequestException(ValidationMessage validationMessage, String debugInfo) {
        super(validationMessage, debugInfo);
    }
}
