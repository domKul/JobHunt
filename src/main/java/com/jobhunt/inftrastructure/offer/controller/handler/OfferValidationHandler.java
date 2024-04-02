package com.jobhunt.inftrastructure.offer.controller.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class OfferValidationHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<OfferErrorResponse>handlerMethodArgumentNotValidException(){


        return ResponseEntity.accepted().build();
    }



}
