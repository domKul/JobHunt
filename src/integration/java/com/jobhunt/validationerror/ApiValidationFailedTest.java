package com.jobhunt.validationerror;

import com.jobhunt.BaseIntegrationTest;
import com.jobhunt.inftrastructure.apivalidation.ApiValidationErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiValidationFailedTest extends BaseIntegrationTest {

    @Test
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
