package com.saly.user;

import com.saly.user.common.exception.ValidationMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerValidationMessages implements ValidationMessage {
    CUSTOMER_CREATION_DATA_REQUIRED("Request body with customer data required"),
    EMAIL_REQUIRED("Field 'email' is required"),
    PASSWORD_REQUIRED("Field 'password' is required"),
    NAME_REQUIRED("Field 'name' is required");

    private final String message;
}
