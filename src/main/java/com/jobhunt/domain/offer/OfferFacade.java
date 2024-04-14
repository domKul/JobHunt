package com.jobhunt.domain.offer;

import com.jobhunt.domain.offer.dto.OfferRequestDto;
import com.jobhunt.domain.offer.dto.OfferResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@RequiredArgsConstructor
public class OfferFacade {

    private final OfferFetchService offerFetchService;
    private final OfferService offerService;
    @Cacheable(cacheNames = "jobOffers")
    public List<OfferResponseDto> findAllOffersFromDb() {
        return offerService.findAllOffers();
    }

    public List<OfferResponseDto> fetchOffersAndSaveAllIfNotExists() {
        return offerFetchService.fetchAllOffersAndSaveAllIfNotExists();
    }

    public OfferResponseDto findOfferByGivenId(String id) {
        return offerService.findOfferById(id);
    }

    public OfferResponseDto saveOffer(OfferRequestDto offerDto) {
        return offerService.save(offerDto);
    }
}
