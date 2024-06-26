package com.jobhunt.domain.userloginandregister.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
 public enum UserExceptionMessage {

    USER_NOT_FOUND("User not found");
    private final String message;

}
