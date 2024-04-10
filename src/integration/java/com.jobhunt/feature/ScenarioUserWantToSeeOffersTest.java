package com.jobhunt.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.jobhunt.BaseIntegrationTest;
import com.jobhunt.JobOffersResponseExample;
import com.jobhunt.domain.offer.dto.OfferRequestDto;
import com.jobhunt.domain.offer.dto.OfferResponseDto;
import com.jobhunt.domain.user.dto.UserDto;
import com.jobhunt.domain.user.dto.UserRegisterDto;
import com.jobhunt.inftrastructure.offer.scheduler.OffersScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

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
        //step 3.1: user tried to generate JWT By requesting POST on /token with username = user and password = password and system return 401 status because the user doesn't exist ind DB
        //Given
        String userInfo =
                """
                {
                "username": "user,
                "password": "password
                }
                """.trim();
        String expectedMessage =
                """
                {
                "message": "Bad Credentials",
                "status": "UNAUTHORIZED"
                }
                """.trim();
        //When
        ResultActions performUser = mockMvc.perform(MockMvcRequestBuilders.post("/token")
                .content(userInfo)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        //Then
        performUser
                .andExpect(status().isUnauthorized())
                        .andExpect(content().json(expectedMessage));


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
        String addedOfferId = offerResponseDto.id();

        assertAll(
                () -> assertThat(addedOfferId).isNotNull(),
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

         //step 8: user made GET /offers/{id} and system returned OK(200) with offer
        // Given && When
        MvcResult resultFromGetById = mockMvc.perform(MockMvcRequestBuilders.get("/offers/" + addedOfferId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        String content = resultFromGetById.getResponse().getContentAsString();
        OfferResponseDto responseById = objectMapper.readValue(content, OfferResponseDto.class);
        assertAll(
                ()->assertThat(responseById.id()).isEqualTo(addedOfferId),
                ()->assertThat(responseById.company()).isEqualTo("company"),
                ()->assertThat(responseById.offerUrl()).isEqualTo("offer/url"),
                ()->assertThat(responseById.position()).isEqualTo("Junior Java"),
                ()->assertThat(responseById.salary()).isEqualTo("6000")
        );
        //step 9: fetch third time offers and got 2 more offers with unique url in total 5 offers
        //Given & When
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(HttpStatus.OK.value())
                        .withBody(bodyWithFourOffers())));
        //Then
        List<OfferResponseDto> anotherTwoOffers = offersScheduler.scheduledFetchOffers();
        assertThat(anotherTwoOffers).hasSize(2);
        // step 10: step 15: user made GET /offers  and system returned OK(200) with 5 offers
        //Given && When && Then
        mockMvc.perform(MockMvcRequestBuilders.get(getOffersUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(5));

    }


    /*

step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status OK(200)
step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000

     */
}
