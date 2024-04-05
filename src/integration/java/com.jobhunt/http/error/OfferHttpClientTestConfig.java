package com.jobhunt.http.error;

import com.jobhunt.domain.offer.OfferProxy;
import com.jobhunt.inftrastructure.offer.http.OfferClientHttpConfig;
import org.springframework.web.client.RestTemplate;

import static com.jobhunt.BaseIntegrationTest.WIRE_MOCK_HOST;

public class OfferHttpClientTestConfig extends OfferClientHttpConfig {

    public OfferProxy offerTestClient(int port, int connectionTimeout, int readTimeout){
        RestTemplate restTemplate = restTemplate(connectionTimeout, readTimeout, restTemplateResponseErrorHandler());
        return offerClient(restTemplate, WIRE_MOCK_HOST,port);
    }

}
