package com.jobhunt.feature;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.jobhunt.BaseIntegrationTest;
import com.jobhunt.JobOffersResponseExample;
import com.jobhunt.domain.offer.dto.OfferRequestDto;
import com.jobhunt.domain.offer.dto.OfferResponseDto;
import com.jobhunt.domain.userloginandregister.dto.UserRegisterDto;
import com.jobhunt.inftrastructure.offer.scheduler.OffersScheduler;
import com.jobhunt.inftrastructure.userloginandregister.controller.JwtResponseDto;
import com.jobhunt.inftrastructure.userloginandregister.controller.TokenRequestDto;
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
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        //step 1.1: scheduler ran 1 time and add 0 offer from external server
        //When
        List<OfferResponseDto> offerResponseDtos = offersScheduler.scheduledFetchOffers();
        //Then
        assertThat(offerResponseDtos).isEmpty();

        //step 2: user tried to generate JWT By requesting POST on /token with username = user and password = password and system return 401 status because the user doesn't exist ind DB
        // given & when
        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        failedLoginRequest
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("""
                        {
                          "message": "Bad Credentials",
                          "status": "UNAUTHORIZED"
                        }
                        """.trim()));

        //step 2.1: user made GET from /offers without jwt token and system return 403(FORBIDDEN)
        //Given
        String getOffersUrl = "/offers";
        //When && Then
        mockMvc.perform(MockMvcRequestBuilders.get(getOffersUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        //step 2.2: user made GET without jwt token from /offers and return List with zero offers with status 403(FORBIDDEN)
        //Given
        //When && Then
        mockMvc.perform(MockMvcRequestBuilders.get(getOffersUrl)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        //step 2.3: user made POST without token /offers  and system returned 03(FORBIDDEN)
        //Given
        OfferRequestDto requestBody1 = OfferRequestDto.builder()
                .company("company")
                .offerUrl("offer/url")
                .position("Junior Java")
                .salary("6000")
                .build();
        //When
        mockMvc.perform(post("/offers")
                .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestBody1)))
                .andExpect(status().isForbidden());

        //step 3: user made POST  /register with username= user and pw= password and system should return 201 CREATED status
        //Given
        String registerUrl = "/register";
        UserRegisterDto user = new UserRegisterDto("user","password");
        //When && Then
        mockMvc.perform(post(registerUrl)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("user"));

        //step 3.1 user made POST /token to generate jwt token with already registered user and system generate token with 201 CREATED status
        //Given
        TokenRequestDto tokenRequestDto = new TokenRequestDto(user.username(),user.password());
        //When
        ResultActions tokenRequest = mockMvc.perform(post("/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRequestDto)));
        //Then
        MvcResult tokenRequestResult = tokenRequest.andExpect(status().isCreated()).andReturn();
        String tokenContent = tokenRequestResult.getResponse().getContentAsString();
        JwtResponseDto tokenJwtResponse = objectMapper.readValue(tokenContent, JwtResponseDto.class);
        String userToken = tokenJwtResponse.token();
        assertAll(
                () -> assertThat(tokenJwtResponse.username()).isEqualTo("user"),
                () -> assertThat(userToken).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$"))
        );

        //step 4: user made GET with jwt token from /offers and return List with zero offers with status 200 OK
        //Given
        //When && Then
        mockMvc.perform(MockMvcRequestBuilders.get(getOffersUrl)
                        .header("Authorization" ,"Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(0))
                .andExpect(status().isOk());

        //step 4.1: user made GET /offers/123456789 and system returned NOT_FOUND(404)
        //Given
        int id = 123456789;
        //When && Then
        mockMvc.perform(MockMvcRequestBuilders.get("/offers/" + id)
                        .header("Authorization" ,"Bearer " + userToken))
                .andExpect(status().isNotFound())
                .andExpect(content().json(
                        """
                                {
                                  "message": "Offer not found with id : 123456789",
                                  "status": "NOT_FOUND"
                                }
                                """.trim()
                ));

        //step 5: user made POST with token /offers  and system returned CREATED(201) with saved offer
        //Given
        OfferRequestDto requestBody2 = OfferRequestDto.builder()
                .company("company")
                .offerUrl("offer/url")
                .position("Junior Java")
                .salary("6000")
                .build();
        //When
        ResultActions performPostOffer = mockMvc.perform(post("/offers")
                .header("Authorization" ,"Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestBody2))
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
                () -> assertThat(offerResponseDto.company()).isEqualTo(requestBody2.company()),
                () -> assertThat(offerResponseDto.salary()).isEqualTo(requestBody2.salary()),
                () -> assertThat(offerResponseDto.position()).isEqualTo(requestBody2.position()),
                () -> assertThat(offerResponseDto.offerUrl()).isEqualTo(requestBody2.offerUrl())
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
        //step 7: user made GET with token /offer and get 3 offers with status OK(200) 1 from database and 2 from external server
        //Given && When && Then
         mockMvc.perform(MockMvcRequestBuilders.get(getOffersUrl)
                         .header("Authorization" ,"Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3));

         //step 8: user made GET with token /offers/{id} and system returned OK(200) with offer
        // Given && When
        MvcResult resultFromGetById = mockMvc.perform(MockMvcRequestBuilders.get("/offers/" + addedOfferId)
                        .header("Authorization" ,"Bearer " + userToken)
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
        // step 10: step 15: user made GET with token /offers  and system returned OK(200) with 5 offers
        //Given && When && Then
        mockMvc.perform(MockMvcRequestBuilders.get(getOffersUrl)
                        .header("Authorization" ,"Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(5));

    }
}
