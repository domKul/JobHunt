package com.jobhunt.domain.offer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
class OfferBeanConfiguration {

    @Bean
    OfferFacade offerFacade(OfferProxy offerProxy, OfferRepository offerRepository){
        OfferService offerservice = new OfferService(offerRepository);
        OfferFetchService fetch = new OfferFetchService(offerProxy,offerservice);
        return new OfferFacade(fetch, offerservice);
    }
}
