package com.jobhunt.inftrastructure.offer.controller.handler;

import org.springframework.http.HttpStatus;

 record PostOfferErrorResponse(String message
        , HttpStatus status) {
}
