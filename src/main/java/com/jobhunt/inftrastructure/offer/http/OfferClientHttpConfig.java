package com.jobhunt.inftrastructure.offer.http;

import com.jobhunt.domain.offer.OfferProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class OfferClientHttpConfig {

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler(){
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(@Value("${offer.http.client.config.connectionTimeout:10000}") long connectionTimeout,
                                     @Value("${offer.http.client.config.readTime:10000}") long readTime,
                                     RestTemplateResponseErrorHandler restTemplateResponseErrorHandler){
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTime))
                .build();
    }

    @Bean
    public OfferProxy offerClient(RestTemplate restTemplate,
                                  @Value("${offer.http.client.config.uri:http://example.com}")String uri,
                                  @Value("${offer.http.client.config.port:5057}") int port){
        return new OfferHttpClient(restTemplate,uri,port);
    }
}
