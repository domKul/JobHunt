package com.jobhunt.domain.offer;

import com.jobhunt.domain.offer.dto.JobOfferResponse;
import com.jobhunt.domain.offer.dto.OfferRequestDto;
import com.jobhunt.domain.offer.dto.OfferResponseDto;

class OfferMapper {

    public static OfferResponseDto mapFromOfferToOfferDto(Offer offer) {
        return OfferResponseDto.builder()
                .id(offer.id())
                .company(offer.company())
                .position(offer.position())
                .salary(offer.salary())
                .offerUrl(offer.offerUrl())
                .build();
    }

    public static Offer mapFromOfferDtoToOffer(OfferRequestDto offerDto) {
        return Offer.builder()
                .company(offerDto.company())
                .position(offerDto.position())
                .salary(offerDto.salary())
                .offerUrl(offerDto.offerUrl())
                .build();
    }

    public static Offer mapFromJobOfferResponseToOffer(JobOfferResponse jobOfferDto) {
        return Offer.builder()
                .offerUrl(jobOfferDto.offerUrl())
                .salary(jobOfferDto.salary())
                .position(jobOfferDto.title())
                .company(jobOfferDto.company())
                .build();
    }
}
