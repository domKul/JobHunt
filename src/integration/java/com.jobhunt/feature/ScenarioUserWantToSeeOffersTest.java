package com.jobhunt.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.jobhunt.BaseIntegrationTest;
import com.jobhunt.JobOffersResponseExample;
import com.jobhunt.domain.offer.dto.OfferRequestDto;
import com.jobhunt.domain.offer.dto.OfferResponseDto;
import com.jobhunt.inftrastructure.offer.scheduler.OffersScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ScenarioUserWantToSeeOffersTest extends BaseIntegrationTest implements JobOffersResponseExample {

    @Autowired
    private OffersScheduler offersScheduler;

    @Container
    public static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("offer.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("offer.http.client.config.port", () -> wireMockServer.getPort());
    }

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

        //step 5: user made POST /offers  and system returned CREATED(201) with saved offer
        //Given
        OfferRequestDto requestBody = OfferRequestDto.builder()
                .company("company")
                .offerUrl("offer/url")
                .position("Junior Java")
                .salary("6000")
                .build();
        //When
        ResultActions performPostOffer = mockMvc.perform(MockMvcRequestBuilders.post("/offers")
                .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestBody))
        );
        //Then
        String contentPostOffer = performPostOffer.andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OfferResponseDto offerResponseDto = objectMapper.readValue(contentPostOffer, OfferResponseDto.class);
        String offerId = offerResponseDto.id();

        assertAll(
                () -> assertThat(offerId).isNotNull(),
                () -> assertThat(offerResponseDto.company()).isEqualTo(requestBody.company()),
                () -> assertThat(offerResponseDto.salary()).isEqualTo(requestBody.salary()),
                () -> assertThat(offerResponseDto.position()).isEqualTo(requestBody.position()),
                () -> assertThat(offerResponseDto.offerUrl()).isEqualTo(requestBody.offerUrl())
        );

        //step 6: trying to fetch offers and got 2 offers real scheduled time 3H
        //Given & When
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.OK.value())
                        .withBody(bodyWithTwoOffers())));

        List<OfferResponseDto> twoOffers = offersScheduler.scheduledFetchOffers();
        //Then
        assertAll(
                () -> assertThat(twoOffers).hasSize(2),
                () -> assertThat(twoOffers.get(0).company()).isEqualTo("BlueSoft Sp. z o.o."),
                () -> assertThat(twoOffers.get(1).company()).isEqualTo("Efigence SA")
        );
        //step 7: user made GET /offer and get 3 offers with status OK(200) 1 from database and 2 from external server
        //Given && When && Then
         mockMvc.perform(MockMvcRequestBuilders.get(getOffersUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3));



    }


    /*

step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers

step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
step 12: user made GET /offers/1000 and system returned OK(200) with offer
step 13: there are 2 new offers in external HTTP server
step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000

     */
}
