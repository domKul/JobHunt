package com.jobhunt.inftrastructure.offer.controller;

import com.jobhunt.domain.offer.OfferFacade;
import com.jobhunt.domain.offer.dto.OfferResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferFacade offerFacade;

    @GetMapping
    ResponseEntity<List<OfferResponseDto>> findOffers(){
        List<OfferResponseDto> responseList = offerFacade.findAllOffersFromDb();
        return ResponseEntity.ok().body(responseList);
    }
}
