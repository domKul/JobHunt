package com.jobhunt.inftrastructure.apivalidation;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
class ApiValidationHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiValidationErrorResponse>handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<String> errorsFromException = getErrorsFromException(ex);
        ApiValidationErrorResponse validationResponse = new ApiValidationErrorResponse(errorsFromException,
                HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest().body(validationResponse);

    }

    private List<String>getErrorsFromException(MethodArgumentNotValidException ex){
        return ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }
}
