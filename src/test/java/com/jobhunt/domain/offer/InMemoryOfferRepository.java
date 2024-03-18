package com.jobhunt.domain.offer;

import com.jobhunt.domain.offer.dto.OfferResponseDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryOfferRepository implements OfferRepository{

    Map<String,Offer> database = new ConcurrentHashMap<>();

    @Override
    public Optional<Offer> findById(String id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public boolean existsByOfferUrl(String url) {
        return database.values()
                .stream()
                .filter(offer -> offer.offerUrl().equals(url))
                .count() == 1;
    }

    @Override
    public List<Offer> saveAll(List<Offer> offers) {
        return offers.stream()
                .map(this::save)
                .toList();
    }

    @Override
    public List<Offer> findAll() {
        return database.values().stream().toList();
    }

    @Override
    public Offer save(Offer offer) {
        UUID id = UUID.randomUUID();
        Offer offerToSave = new Offer(
                id.toString(),
                offer.companyName(),
                offer.salary(),
                offer.position(),
                offer.offerUrl()
        );
        database.put(id.toString(), offerToSave);
        return  offerToSave;
    }
}
