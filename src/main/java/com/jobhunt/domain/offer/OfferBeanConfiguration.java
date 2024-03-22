package com.jobhunt.domain.offer;

import com.jobhunt.domain.offer.dto.JobOfferResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
public class OfferBeanConfiguration {


    @Bean
    OfferFacade offerFacade(OfferProxy offerProxy){
        OfferRepository offerRepository = new OfferRepository() {
            @Override
            public Optional<Offer> findById(String id) {
                return Optional.empty();
            }

            @Override
            public boolean existsByOfferUrl(String s) {
                return false;
            }

            @Override
            public List<Offer> saveAll(List<Offer> offers) {
                return null;
            }

            @Override
            public List<Offer> findAll() {
                return null;
            }

            @Override
            public Offer save(Offer offer) {
                return null;
            }
        };
        OfferService offerservice = new OfferService(offerRepository);

        OfferFetchService fetch = new OfferFetchService(offerProxy,offerservice);
        return new OfferFacade(fetch, offerservice);
    }
}
