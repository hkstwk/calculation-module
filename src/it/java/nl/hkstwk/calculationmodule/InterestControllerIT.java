package nl.hkstwk.calculationmodule;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InterestControllerIT extends AbstractIT {

    @Test
    void testCompoundInterest() throws Exception {
        Path directoryPath = Paths.get(ResourceUtils.getFile(jsonDirectory).toURI());

        String inputJson = Files.readString(directoryPath.resolve("CompoundInterestRequest.json"));
        String outputJson = Files.readString(directoryPath.resolve("CompoundInterestResponse.json"));

        mockMvc.perform(post("/interest/compound")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isNotFound())
                .andExpect(content().json(outputJson));
    }

    @Test
    void testCompoundInterestWithDetails() throws Exception {
        Path directoryPath = Paths.get(ResourceUtils.getFile(jsonDirectory).toURI());

        String inputJson = Files.readString(directoryPath.resolve("CompoundInterestRequest.json"));
        String outputJson = Files.readString(directoryPath.resolve("CompoundInterestWithDetailsResponse.json"));

        mockMvc.perform(post("/interest/compound-with-details")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk())
                .andExpect(content().json(outputJson));
    }
}
