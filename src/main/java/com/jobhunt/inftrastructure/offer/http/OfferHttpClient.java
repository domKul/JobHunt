package com.jobhunt.inftrastructure.offer.http;

import com.jobhunt.domain.offer.OfferProxy;
import com.jobhunt.domain.offer.dto.JobOfferResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
@Slf4j
@AllArgsConstructor
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
            String urlForService = getUrlForService();
            final String url = UriComponentsBuilder.fromHttpUrl(urlForService).toUriString();
            ResponseEntity<List<JobOfferResponse>> exchange = restTemplate.exchange(url, HttpMethod.GET,
                    objectHttpEntity, new ParameterizedTypeReference<>() {
                    });
           final List<JobOfferResponse> body = exchange.getBody();
            if (body == null){
                log.warn("Body is null");
               throw new ResponseStatusException(HttpStatus.NO_CONTENT);
           }
            log.info("Success response " + body);
            return body;
        }catch (ResourceAccessException e){
            log.error("Cannot fetch offers" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getUrlForService() {
        return uri + ":" + port + "/offers";
    }
}
