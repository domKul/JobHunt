package com.jobhunt.domain.offer;

import com.jobhunt.domain.offer.dto.JobOfferResponse;

import java.util.List;

interface OfferFetch {
    List<JobOfferResponse>fetchOffers();
}
