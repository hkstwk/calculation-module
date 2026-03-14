package nl.hkstwk.calculationmodule.services;

import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import nl.hkstwk.calculationmodule.utils.calculators.CompoundInterestCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.assertj.core.api.Assertions.assertThat;

class InterestServiceTest {

    private InterestService interestService;

    @BeforeEach
    void setUp() {
        CompoundInterestCalculator compoundInterestCalculator = new CompoundInterestCalculator();
        interestService = new InterestService(compoundInterestCalculator, new ObjectMapper());
    }

    @Test
    void testCompoundInterestWithDetailsCalculator() {

        CompoundInterestRequestDto request = CompoundInterestRequestDto.builder()
                .time(1)
                .originalPrincipalSum(BigDecimal.valueOf(10_000.00))
                .monthlyDeposit(BigDecimal.valueOf(0))
                .compoundingFrequency(1)
                .nominalAnnualInterestRate(BigDecimal.valueOf(0.03))
                .includeDetails(true)
                .depositAtStart(true)
                .build();

        CompoundInterestResponseDto responseDto = interestService.compoundInterestCalculation(request);

        assertThat(responseDto.getFinalAmount()).isEqualTo(BigDecimal.valueOf(10_300).setScale(2, RoundingMode.HALF_UP));
        assertThat(responseDto.getDetails()).hasSize(1);
    }
}