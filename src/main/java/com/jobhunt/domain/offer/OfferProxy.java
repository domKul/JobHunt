package com.jobhunt.domain.offer;

import com.jobhunt.domain.offer.dto.JobOfferResponse;

import java.util.List;

interface OfferProxy {
    List<JobOfferResponse>fetchOffers();
}
