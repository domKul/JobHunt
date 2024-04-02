package com.jobhunt.domain.offer;

import com.jobhunt.domain.offer.dto.OfferRequestDto;
import com.jobhunt.domain.offer.dto.OfferResponseDto;
import com.jobhunt.domain.offer.exception.OfferExceptionMessages;
import com.jobhunt.domain.offer.exception.OfferNotFoundException;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
@RequiredArgsConstructor
class OfferService {

    private final OfferRepository offerRepository;

    public List<OfferResponseDto> findAllOffers() {
        return offerRepository.findAll()
                .stream()
                .map(OfferMapper::mapFromOfferToOfferDto)
                .collect(Collectors.toList());
    }

    public OfferResponseDto findOfferById(String id) {
        return offerRepository.findById(id)
                .map(OfferMapper::mapFromOfferToOfferDto)
                .orElseThrow(() -> new OfferNotFoundException(OfferExceptionMessages.OFFER_NOT_FOUND
                        .getMessage() + " with id : " + id));
    }

    public OfferResponseDto save(OfferRequestDto offerDto) {
        final Offer offer = OfferMapper.mapFromOfferDtoToOffer(offerDto);
        final Offer save = offerRepository.save(offer);
        return OfferMapper.mapFromOfferToOfferDto(save);
    }

    boolean existsByOfferUrl(String s){
        return offerRepository.existsByOfferUrl(s);
    }

     List<OfferResponseDto> saveAllJobs(List<Offer> offers) {
        return offerRepository.saveAll(offers).stream()
                .map(OfferMapper::mapFromOfferToOfferDto)
                .toList();
    }
}
