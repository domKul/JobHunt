package com.jobhunt.domain.offer;

import com.jobhunt.domain.offer.dto.JobOfferResponse;
import lombok.AllArgsConstructor;

import java.util.List;
@AllArgsConstructor
class InMemoryFetchTestImpl implements OfferProxy {

    List<JobOfferResponse>responseList;

    @Override
    public List<JobOfferResponse> fetchOffers() {
        return responseList;
    }
}
