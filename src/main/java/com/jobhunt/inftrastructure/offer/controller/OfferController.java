package com.jobhunt.inftrastructure.offer.controller;

import com.jobhunt.domain.offer.OfferFacade;
import com.jobhunt.domain.offer.dto.OfferRequestDto;
import com.jobhunt.domain.offer.dto.OfferResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferFacade offerFacade;

    @GetMapping
    ResponseEntity<List<OfferResponseDto>> findOffers() {
        List<OfferResponseDto> responseList = offerFacade.findAllOffersFromDb();
        return ResponseEntity.ok().body(responseList);
    }

    @GetMapping("/{id}")
    ResponseEntity<OfferResponseDto> findOfferById(@PathVariable String id) {
        OfferResponseDto offerResponse = offerFacade.findOfferByGivenId(id);
        return ResponseEntity.ok().body(offerResponse);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<OfferResponseDto>addOffer(@RequestBody @Valid OfferRequestDto offerRequest){
        OfferResponseDto offerResponseDto = offerFacade.saveOffer(offerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(offerResponseDto);
    }
}
