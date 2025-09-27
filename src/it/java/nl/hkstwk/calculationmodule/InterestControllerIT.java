package nl.hkstwk.calculationmodule;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InterestControllerIT extends AbstractIT {
    private static final String REQUEST_PATH = "__files/compound-interest/request/";
    private static final String RESPONSE_PATH = "__files/compound-interest/response/";

    @Test
    void testCompoundInterest() throws Exception {
        mockMvc.perform(post("/interest/compound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loadResource(REQUEST_PATH + "CompoundInterestRequestNoDetails.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(loadResource(RESPONSE_PATH + "CompoundInterestResponse.json")));
    }

    @Test
    void testCompoundInterestWithInvalidRequest() throws Exception {
        mockMvc.perform(post("/interest/compound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loadResource(REQUEST_PATH + "CompoundInterestInvalidRequest.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(loadResource(RESPONSE_PATH + "BadRequestResponse.json")));
    }

    @Test
    void testCompoundInterestWithDetails() throws Exception {
        mockMvc.perform(post("/interest/compound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loadResource(REQUEST_PATH + "CompoundInterestRequestIncludeDetails.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(loadResource(RESPONSE_PATH + "CompoundInterestWithDetailsResponse.json")));
    }
}
