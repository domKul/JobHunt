package com.jobhunt.domain.registerandlogin.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserNotFoundException extends RuntimeException {
    private final UserExceptionMessage userExceptionMessage;


}
