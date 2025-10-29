package nl.hkstwk.calculationmodule;

import nl.hkstwk.calculationmodule.repositories.CalculationRequestRepository;
import nl.hkstwk.calculationmodule.repositories.UserCrudRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InterestControllerIT extends AbstractIT {
    private static final String REQUEST_PATH = "__files/compound-interest/request/";
    private static final String RESPONSE_PATH = "__files/compound-interest/response/";

    @Autowired
    private UserCrudRepository userCrudRepository;

    @Autowired
    private CalculationRequestRepository calculationRequestRepository;

    @BeforeEach
    void setUp() {
        userCrudRepository.deleteAll();
        calculationRequestRepository.deleteAll();
    }

    @Test
    void testCompoundInterest() throws Exception {
        mockMvc.perform(post("/interest/compound")
                        .with(jwtWithSubjectAndAuthority("user1", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loadResource(REQUEST_PATH + "CompoundInterestRequestNoDetails.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(loadResource(RESPONSE_PATH + "CompoundInterestResponse.json")));

        Assertions.assertTrue(userCrudRepository.findByUsername("user1").isPresent());
        assertThat(userCrudRepository.count()).isEqualTo(1);
        assertThat(calculationRequestRepository.count()).isEqualTo(1);
    }

    @Test
    void testCompoundInterestWithInvalidRequest() throws Exception {
        mockMvc.perform(post("/interest/compound")
                        .with(jwtWithSubjectAndAuthority("user2", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loadResource(REQUEST_PATH + "CompoundInterestInvalidRequest.json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(loadResource(RESPONSE_PATH + "BadRequestResponse.json")));

        assertThat(userCrudRepository.findByUsername("user2").isPresent()).isFalse();
        assertThat(userCrudRepository.count()).isZero();
        assertThat(calculationRequestRepository.count()).isZero();
    }

    @Test
    void testCompoundInterestWithDetails() throws Exception {
        mockMvc.perform(post("/interest/compound")
                        .with(jwtWithSubjectAndAuthority("user3", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loadResource(REQUEST_PATH + "CompoundInterestRequestIncludeDetails.json")))
                .andExpect(status().isOk())
                .andExpect(content().json(loadResource(RESPONSE_PATH + "CompoundInterestWithDetailsResponse.json")));

        Assertions.assertTrue(userCrudRepository.findByUsername("user3").isPresent());
        assertThat(userCrudRepository.count()).isEqualTo(1);
        assertThat(calculationRequestRepository.count()).isEqualTo(1);
    }

    @Test
    void testCompoundInterestWithUnauthenticatedUserShouldGiveStatus401() throws Exception {
        mockMvc.perform(post("/interest/compound")
                        .header("Authorization", "Bearer invalid-token-here")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loadResource(REQUEST_PATH + "CompoundInterestInvalidRequest.json")))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value( "Requires authentication"));

        assertThat(userCrudRepository.count()).isZero();
        assertThat(calculationRequestRepository.count()).isZero();
    }

    @Test
    void testCompoundInterestWithUnauthorizedUserShouldGiveStatus403() throws Exception {
        mockMvc.perform(post("/interest/compound")
                        .with(jwtWithSubjectAndAuthority("user4", "USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loadResource(REQUEST_PATH + "CompoundInterestInvalidRequest.json")))
                .andExpect(status().isForbidden());

        assertThat(userCrudRepository.count()).isZero();
        assertThat(calculationRequestRepository.count()).isZero();
    }
}
