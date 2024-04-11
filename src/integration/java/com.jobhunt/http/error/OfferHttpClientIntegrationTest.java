package com.jobhunt.http.error;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.jobhunt.JobOffersResponseExample;
import com.jobhunt.domain.offer.OfferProxy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OfferHttpClientIntegrationTest implements JobOffersResponseExample {

    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String APPLICATION_JSON_CONTENT = "application/json";
    public static final String SERVER_ERROR_MESSAGE = "500 INTERNAL_SERVER_ERROR";

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    OfferProxy remoteOfferClient = new OfferHttpClientTestConfigBean().offerTestClient(wireMockServer.getPort(),
            1000,1000);

    @Test
    void shouldReturn500_INTERNAL_SERVER_ERROR_WhenFaultConnectionByPeer(){
        //Given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER,APPLICATION_JSON_CONTENT)
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));
        //When
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> remoteOfferClient.fetchOffers());

        //Then
        assertEquals(responseStatusException.getMessage(),SERVER_ERROR_MESSAGE);
    }

    @Test
    void shouldReturn500_INTERNAL_SERVER_ERROR_WhenFaultEmptyResponse(){
        //Given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER,APPLICATION_JSON_CONTENT)
                        .withFault(Fault.EMPTY_RESPONSE)));
        //When
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> remoteOfferClient.fetchOffers());

        //Then
        assertEquals(responseStatusException.getMessage(),SERVER_ERROR_MESSAGE);
    }

    @Test
    void shouldReturn500_INTERNAL_SERVER_ERROR_WhenFaultMalformedResponseChunk(){
        //Given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER,APPLICATION_JSON_CONTENT)
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));
        //When
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> remoteOfferClient.fetchOffers());

        //Then
        assertEquals(responseStatusException.getMessage(),SERVER_ERROR_MESSAGE);
    }

    @Test
    void shouldReturn500_INTERNAL_SERVER_ERROR_WhenFaultRandomDataThenClose(){
        //Given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE_HEADER,APPLICATION_JSON_CONTENT)
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)));
        //When
        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> remoteOfferClient.fetchOffers());

        //Then
        assertEquals(responseStatusException.getMessage(),SERVER_ERROR_MESSAGE);
    }
}
