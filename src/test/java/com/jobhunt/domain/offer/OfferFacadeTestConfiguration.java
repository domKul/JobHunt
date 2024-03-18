package com.jobhunt.domain.offer;

import com.jobhunt.domain.offer.dto.JobOfferResponse;

import java.util.List;


class OfferFacadeTestConfiguration {

    private final InMemoryFetchTestImpl inMemoryFetchTest;
    private final InMemoryOfferRepository inMemoryOfferRepository;

     OfferFacadeTestConfiguration() {
        this.inMemoryFetchTest = new InMemoryFetchTestImpl(
                List.of(
                        new JobOfferResponse("title1","company1","salary1","url1"),
                        new JobOfferResponse("title2","company2","salary2","url2"),
                        new JobOfferResponse("title3","company3","salary3","url3"),
                        new JobOfferResponse("title4","company4","salary4","url4"),
                        new JobOfferResponse("title5","company5","salary5","url5")
                )
        );
        this.inMemoryOfferRepository = new InMemoryOfferRepository();
    }

    public OfferFacadeTestConfiguration(List<JobOfferResponse> remoteOffers) {
        this.inMemoryFetchTest = new InMemoryFetchTestImpl(remoteOffers);
        this.inMemoryOfferRepository = new InMemoryOfferRepository();
    }

    OfferFacade offerFacadeTests(){
         return new OfferFacade(new OfferFetchService(inMemoryFetchTest,inMemoryOfferRepository),
                 new OfferService(inMemoryOfferRepository));
    }
}
