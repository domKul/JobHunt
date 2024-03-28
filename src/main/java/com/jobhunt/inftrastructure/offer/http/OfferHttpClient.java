package com.jobhunt.inftrastructure.offer.http;

import com.jobhunt.domain.offer.OfferProxy;
import com.jobhunt.domain.offer.dto.JobOfferResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
@RequiredArgsConstructor
@Slf4j
public class OfferHttpClient implements OfferProxy {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;
    @Override
    public List<JobOfferResponse> fetchOffers() {
        log.info("Started fetching offers");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<HttpHeaders> objectHttpEntity = new HttpEntity<>(headers);
        try{
            String urlForService = getUrlForService("/offers");
            final String url = UriComponentsBuilder.fromHttpUrl(urlForService).toUriString();
            ResponseEntity<List<JobOfferResponse>> exchange = restTemplate.exchange(url, HttpMethod.GET,
                    objectHttpEntity, new ParameterizedTypeReference<>() {
                    });
           final List<JobOfferResponse> body = exchange.getBody();
            if (body == null){
                log.warn("No offers");
               throw new ResponseStatusException(HttpStatus.NO_CONTENT);
           }
            return body;
        }catch (ResourceAccessException e){
            log.error("Cannot fetch offers" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getUrlForService(String service) {
        return uri + ":" + port + service;
    }
}
