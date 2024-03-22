package com.jobhunt.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.jobhunt.BaseIntegrationTest;
import com.jobhunt.JobOffersResponseExample;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ScenarioUserWantToSeeOffersTest extends BaseIntegrationTest implements JobOffersResponseExample {

    @Test
    void should_user_external_server_for_fetch_offers() {
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type","application-json")
                        .withStatus(HttpStatus.OK.value())
                        .withBody(bodyWithoutOffers())));

    }
}
