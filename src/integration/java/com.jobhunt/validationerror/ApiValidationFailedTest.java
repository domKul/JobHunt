package com.jobhunt.validationerror;

import com.jobhunt.BaseIntegrationTest;
import com.jobhunt.inftrastructure.apivalidation.ApiValidationErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiValidationFailedTest extends BaseIntegrationTest {

    @Container
    public static final MongoDBContainer mongoDBContainer =
            new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    @WithMockUser
    void shouldValidationErrorMessageAnd400BadRequestWithEmptyAndNullInRequest() throws Exception {
        //Given
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.post("/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
                .content(
                        """
                                {
                                    "title": "",
                                    "company": "",
                                    "salary": "",
                                    "offerUrl": ""
                                }
                         """.trim()
                ));
        //When
        MvcResult mvcResult = perform.andExpect(status().isBadRequest()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorResponse errors = objectMapper.readValue(content,
                ApiValidationErrorResponse.class);
        //Then
        assertThat(errors.messages()).containsExactlyInAnyOrder(
                "company name mut not be empty",
                "salary mut not be empty",
                "position mut not be empty",
                "position must not be null",
                "url mut not be empty"
        );
    }
}
