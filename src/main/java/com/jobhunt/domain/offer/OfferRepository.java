package com.jobhunt.domain.offer;

import java.util.List;
import java.util.Optional;

interface OfferRepository {
    Optional<Offer> findById(String id);

    boolean existsByOfferUrl(String s);

    List<Offer> saveAll(List<Offer> offers);


    List<Offer> findAll();

    Offer save(Offer offer);
}
