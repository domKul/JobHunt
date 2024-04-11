package com.jobhunt.inftrastructure.userloginandregister.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class TokenControllerErrorHandler {

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<TokenErrorResponse>handleBadCredentials(){
        TokenErrorResponse tokenErrorResponse =
                new TokenErrorResponse(ResponseErrorMessage.BAD_CREDENTIALS.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(tokenErrorResponse);
    }
}
