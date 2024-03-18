package com.jobhunt.domain.offer.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record OfferRequestDto(
                              String companyName,
                              String position,
                              String salary,
                              String offerUrl)
        implements Serializable {
}
