package com.jobhunt.inftrastructure.offer.scheduler;

import com.jobhunt.domain.offer.OfferFacade;
import com.jobhunt.domain.offer.dto.OfferResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OffersScheduler {

    private final OfferFacade offerFacade;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedDelayString = "${http.offers.scheduler.request.delay}")
    public List<OfferResponseDto>scheduledFetchOffers(){
        log.info("fetching offers {}", dateFormat);
        return offerFacade.fetchOffersAndSaveAllIfNotExists();
    }
}
