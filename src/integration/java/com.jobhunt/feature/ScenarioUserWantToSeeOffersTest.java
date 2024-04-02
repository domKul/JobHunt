package com.jobhunt.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.jobhunt.BaseIntegrationTest;
import com.jobhunt.JobOffersResponseExample;
import com.jobhunt.domain.offer.dto.OfferRequestDto;
import com.jobhunt.domain.offer.dto.OfferResponseDto;
import com.jobhunt.inftrastructure.offer.scheduler.OffersScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        //step 3: should GET from /offers and return List with zero offers
        //Given
        String getOffersUrl = "/offers";
        //When && Then
        mockMvc.perform(MockMvcRequestBuilders.get(getOffersUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(0))
                .andExpect(status().isOk());
        //step 4: user made GET /offers/9999 and system returned NOT_FOUND(404)
        //Given
        //When && Then
        mockMvc.perform(MockMvcRequestBuilders.get("/offers/" + 99999))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                                {
                                  "message": "Offer not found with id : 99999",
                                  "status": "NOT_FOUND"
                                }
                                """.trim()
                ));

        //step 5:step 16: user made POST /offers  and system returned CREATED(201) with saved offer
        //Given
        OfferRequestDto requestBody = OfferRequestDto.builder()
                .company("company")
                .offerUrl("offer/url")
                .position("Junior Java")
                .salary("6000")
                .build();
        //When
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/offers")
                .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestBody))
        );
        //Then
        String content = perform.andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OfferResponseDto offerResponseDto = objectMapper.readValue(content, OfferResponseDto.class);
        String offerId = offerResponseDto.id();

        assertAll(
                () -> assertThat(offerId).isNotNull(),
                () ->assertThat(offerResponseDto.company()).isEqualTo(requestBody.company()),
                () ->assertThat(offerResponseDto.salary()).isEqualTo(requestBody.salary()),
                () ->assertThat(offerResponseDto.position()).isEqualTo(requestBody.position()),
                () ->assertThat(offerResponseDto.offerUrl()).isEqualTo(requestBody.offerUrl())
        );


    }
}
