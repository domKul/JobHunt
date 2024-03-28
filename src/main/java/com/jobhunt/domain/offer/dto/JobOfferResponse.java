package com.jobhunt.domain.offer.dto;

public record JobOfferResponse(
        String title,
        String company,
        String salary,
        String offerUrl
) {
}
