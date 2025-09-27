package nl.hkstwk.calculationmodule;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InterestControllerIT extends AbstractIT {

    @Test
    void testCompoundInterest() throws Exception {
        mockMvc.perform(post("/interest/compound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loadResource("__files/interest/CompoundInterestRequestNoDetails.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(loadResource("__files/interest/CompoundInterestResponse.json")));
    }

    @Test
    void testCompoundInterestWithInvalidRequest() throws Exception {
        mockMvc.perform(post("/interest/compound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loadResource("__files/interest/CompoundInterestInvalidRequest.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(loadResource("__files/interest/BadRequestResponse.json")));
    }

    @Test
    void testCompoundInterestWithDetails() throws Exception {
        mockMvc.perform(post("/interest/compound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loadResource("__files/interest/CompoundInterestRequestIncludeDetails.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(loadResource("__files/interest/CompoundInterestWithDetailsResponse.json")));
    }
}
