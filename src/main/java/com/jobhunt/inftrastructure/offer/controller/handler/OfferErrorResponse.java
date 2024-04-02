package com.jobhunt.inftrastructure.offer.controller.handler;

import org.springframework.http.HttpStatus;

record OfferErrorResponse(String message,
                          HttpStatus status) {
}
