package com.jobhunt.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.jobhunt.BaseIntegrationTest;
import com.jobhunt.JobOffersResponseExample;
import com.jobhunt.inftrastructure.offer.scheduler.OffersScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class ScenarioUserWantToSeeOffersTest extends BaseIntegrationTest implements JobOffersResponseExample {

    @Autowired
    private OffersScheduler offersScheduler;

    @Test
    void should_use_external_server_for_fetch_offers_and_return_zero_offers() {
        //Given
        //step 1 : trying to fetch but there are no offers to fetch
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.OK.value())
                        .withBody(bodyWithoutOffers())));
        //When step 2: scheduler ran 1 time and add 0 offer from external server
        //offersScheduler.scheduledFetchOffers();

    }
}
