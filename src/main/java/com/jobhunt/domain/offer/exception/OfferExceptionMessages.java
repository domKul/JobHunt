package com.jobhunt.domain.offer.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OfferExceptionMessages {
    OFFER_NOT_FOUND("Offer not found");
    private final String message;
}
