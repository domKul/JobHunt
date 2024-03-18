package com.jobhunt.domain.offer.dto;

public record JobOfferResponse(
        String title,
        String companyName,
        String salary,
        String offerUrl
) {
}
