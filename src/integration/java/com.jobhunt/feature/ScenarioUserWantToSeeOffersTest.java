package com.jobhunt.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.jobhunt.BaseIntegrationTest;
import com.jobhunt.JobOffersResponseExample;
import com.jobhunt.domain.offer.dto.OfferResponseDto;
import com.jobhunt.inftrastructure.offer.scheduler.OffersScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ScenarioUserWantToSeeOffersTest extends BaseIntegrationTest implements JobOffersResponseExample {

    @Autowired
    private OffersScheduler offersScheduler;

    @Test
    void should_use_external_server_for_fetch_offers_and_return_zero_offers() throws Exception {
        //step 1: trying to fetch but there are no offers to fetch
        //Given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.OK.value())
                        .withBody(bodyWithoutOffers())));
        //step 2: scheduler ran 1 time and add 0 offer from external server
        //When
        List<OfferResponseDto> offerResponseDtos = offersScheduler.scheduledFetchOffers();
        //Then
        assertThat(offerResponseDtos).isEmpty();
        //step 3:
        //Given
        String getOffersUrl = "/offers";
        //When && Then
        mockMvc.perform(MockMvcRequestBuilders.get(getOffersUrl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(0))
                .andExpect(status().isOk());


    }
}
