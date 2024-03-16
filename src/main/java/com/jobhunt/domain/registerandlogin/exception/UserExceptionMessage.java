package com.jobhunt.domain.registerandlogin.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
 public enum UserExceptionMessage {
    USER_NOT_FOUND("User not found");
    private final String message;

    public String getMessage() {
        return message;
    }
}
