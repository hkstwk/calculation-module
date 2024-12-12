package nl.hkstwk.calculationmodule;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class InterestControllerIT extends AbstractIT {

    @Test
    void testCompoundInterest() throws Exception {
        String inputJson = Files.readString(requestDirectory.resolve("CompoundInterestRequestNoDetails.json"));
        String outputJson = Files.readString(responseDirectory.resolve("CompoundInterestResponse.json"));

        mockMvc.perform(post("/interest/compound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }

    @Test
    void testCompoundInterestWithInvalidRequest() throws Exception {
        String inputJson = Files.readString(requestDirectory.resolve("CompoundInterestInvalidRequest.json"));
        String outputJson = Files.readString(responseDirectory.resolve("BadRequestResponse.json"));

        mockMvc.perform(post("/interest/compound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(outputJson));
    }

    @Test
    void testCompoundInterestWithDetails() throws Exception {
        String inputJson = Files.readString(requestDirectory.resolve("CompoundInterestRequestIncludeDetails.json"));
        String outputJson = Files.readString(responseDirectory.resolve("CompoundInterestWithDetailsResponse.json"));

        mockMvc.perform(post("/interest/compound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }
}
