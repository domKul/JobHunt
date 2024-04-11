package com.jobhunt.inftrastructure.userloginandregister.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum ResponseErrorMessage {

     BAD_CREDENTIALS( "Bad Credentials");

    private final String message;
}
