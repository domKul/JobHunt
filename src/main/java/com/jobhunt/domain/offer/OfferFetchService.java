package com.jobhunt.domain.offer;

import com.jobhunt.domain.offer.dto.OfferResponseDto;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class OfferFetchService {

    private final OfferFetch offerFetch;
    private final OfferRepository offerRepository;

    List<OfferResponseDto> fetchAllOffersAndSaveAllIfNotExists() {
        List<Offer> jobOffers = fetchOffers();
        final List<Offer> offers = filterNotExistingOffers(jobOffers);
        return offerRepository.saveAll(offers).stream()
                .map(OfferMapper::mapFromOfferToOfferDto)
                .toList();
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
                .filter(offerDto -> !offerRepository.existsByOfferUrl(offerDto.offerUrl()))
                .collect(Collectors.toList());
    }
}
