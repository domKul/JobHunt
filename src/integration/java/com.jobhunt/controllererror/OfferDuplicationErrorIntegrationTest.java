package com.jobhunt.controllererror;

import com.jobhunt.BaseIntegrationTest;
import com.jobhunt.domain.offer.dto.OfferRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OfferDuplicationErrorIntegrationTest extends BaseIntegrationTest {

    @Container
    public static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void shouldThrowExceptionWhenOfferWithSameUrlAlreadyExistInDb() throws Exception {
        //step 1: creating and saving offer to database
        // Given
        OfferRequestDto requestBody = OfferRequestDto.builder()
                .company("company")
                .offerUrl("offer/url")
                .position("Junior Java")
                .salary("6000")
                .build();
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/offers")
                .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestBody))
        );
        // step 1.1: expecting status 201
        // When
        perform.andExpect(status().isCreated());

        // step 2; expecting conflict status and message "Offer url already exist"
        ResultActions secondPerform = mockMvc.perform(MockMvcRequestBuilders.post("/offers")
                .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                .content(objectMapper.writeValueAsString(requestBody))
        );
        // Then
        MvcResult secondResult = secondPerform.andExpect(status().isConflict()).andReturn();
        String content = secondResult.getResponse().getContentAsString();
        assertEquals("{\"message\":\"Offer url already exist\",\"status\":\"CONFLICT\"}",content);
    }
}
