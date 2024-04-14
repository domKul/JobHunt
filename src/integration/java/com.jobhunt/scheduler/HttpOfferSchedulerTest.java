package com.jobhunt.scheduler;

import com.jobhunt.BaseIntegrationTest;
import com.jobhunt.JobHuntSpringApplication;
import com.jobhunt.domain.offer.OfferProxy;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = JobHuntSpringApplication.class, properties = "scheduling.enabled=true")
class HttpOfferSchedulerTest extends BaseIntegrationTest {

    @SpyBean
    OfferProxy offerHttpClient;

    @Container
    public static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void shouldFetchOfferByScheduledMethod(){
        await()
                .atMost(Duration.ofSeconds(2))
                .untilAsserted(()->verify(offerHttpClient, times(1)).fetchOffers());
    }
}
