package com.jobhunt.inftrastructure.offer.controller.handler;

import com.jobhunt.domain.offer.exception.OfferNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
class OfferHttpErrorHandler {

    @ExceptionHandler(OfferNotFoundException.class)
    ResponseEntity<OfferErrorResponse>handleOfferNotFound(OfferNotFoundException offerNotFoundException){
        OfferErrorResponse response = new OfferErrorResponse(offerNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        log.warn(response.message());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
