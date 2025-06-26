package org.example.authservice.constant;

import lombok.Data;
import lombok.Getter;
import org.example.authservice.dto.ErrorResponse;

@Getter
public enum ErrorMessage {
    UNAUTHORIZATION(401, "INVALID TOKEN "),
    INVALID_PASSWORD(401, "INVALID PASSWORD "),
    EMAIL_IS_NOT_FOUND(401, "EMAIL IS EXISTS "),
    EMAIL_EXISTS(401, "EMAIL ALREADY EXISTS "),
    USER_NOT_FOUND(401, "USER NOT FOUND "),
    INVALID_TOKEN(401, "INVALID TOKEN ");

    private int code;
    private String message;

    // Constructor
    ErrorMessage(int status, String message) {
        this.code = status;
        this.message = message;
    }

    // Optionally, you can add a method to return an ErrorResponse
    public ErrorResponse toErrorResponse() {
        return new ErrorResponse(this.code, this.message);
    }
}

