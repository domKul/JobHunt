package com.jobhunt.scheduler;

import com.jobhunt.BaseIntegrationTest;
import com.jobhunt.JobHuntSpringApplication;
import com.jobhunt.domain.offer.OfferProxy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = JobHuntSpringApplication.class, properties = "scheduling.enabled=true")
class HttpOfferSchedulerTest extends BaseIntegrationTest {

    @SpyBean
    OfferProxy offerHttpClient;

    @Test
    void shouldFetchOfferByScheduledMethod(){
        await()
                .atMost(Duration.ofSeconds(2))
                .untilAsserted(()->verify(offerHttpClient, times(1)).fetchOffers());
    }
}
