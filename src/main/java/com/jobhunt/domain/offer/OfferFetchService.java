package com.jobhunt.domain.offer;

import com.jobhunt.domain.offer.dto.OfferResponseDto;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class OfferFetchService {

    private final OfferProxy offerFetch;
    private final OfferService offerService;

    List<OfferResponseDto> fetchAllOffersAndSaveAllIfNotExists() {
        List<Offer> jobOffers = fetchOffers();
        final List<Offer> offers = filterNotExistingOffers(jobOffers);
        return offerService.saveAllJobs(offers);
    }

    private List<Offer> fetchOffers() {
        return offerFetch.fetchOffers()
                .stream()
                .map(OfferMapper::mapFromJobOfferResponseToOffer)
                .toList();
    }

    private List<Offer> filterNotExistingOffers(List<Offer> jobOffers) {
        return jobOffers.stream()
                .filter(offerDto -> !offerDto.offerUrl().isEmpty())
                .filter(offerDto -> !offerService.existsByOfferUrl(offerDto.offerUrl()))
                .collect(Collectors.toList());
    }
}
