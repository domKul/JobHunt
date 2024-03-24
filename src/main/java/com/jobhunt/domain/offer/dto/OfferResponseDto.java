package com.jobhunt.domain.offer.dto;

import lombok.Builder;

@Builder
public record OfferResponseDto(String id,
                               String company,
                               String salary,
                               String position,
                               String offerUrl
) {
}
