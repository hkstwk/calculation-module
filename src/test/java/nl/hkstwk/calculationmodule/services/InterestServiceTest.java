package nl.hkstwk.calculationmodule.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestWithDetailsResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
class InterestServiceTest {
    @InjectMocks
    private InterestService interestService;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    public static Stream<Arguments> compoundInterestCalculatorInput(){
        return Stream.of(
                arguments(1, 1, 0.03, 10_000.00, 10_300.00),
                arguments(1, 12, 0.03, 10_000.00, 10_304.16),
                arguments(2, 1, 0.03, 10_000.00, 10_609.00),
                arguments(2, 12, 0.03, 10_000.00, 10_617.57),
                arguments(1, 1, 0.04, 10_000, 10_400),
                arguments(1, 12, 0.04, 10_000, 10_407.42),
                arguments(2, 12, 0.07, 10_000, 11_498.06)
        );
    }

    @ParameterizedTest
    @MethodSource("compoundInterestCalculatorInput")
    public void testCompoundInterestCalculator(int time, int frequency, double nominalInterest, double originalPrincipalSum, double accumulatedValue) throws JsonProcessingException {
        CompoundInterestRequestDto request = CompoundInterestRequestDto.builder()
                .time(time)
                .originalPrincipalSum(BigDecimal.valueOf(originalPrincipalSum))
                .compoundingFrequency(frequency)
                .nominalAnnualInterestRate(BigDecimal.valueOf(nominalInterest))
                .build();

        CompoundInterestResponseDto responseDto = interestService.compoundInterestCalculation(request);

        assertThat(responseDto.getFinalAmount()).isEqualTo(BigDecimal.valueOf(accumulatedValue).setScale(2, RoundingMode.HALF_UP));
    }

    @ParameterizedTest
    @MethodSource("compoundInterestCalculatorInput")
    void testCompoundInterestWithDetailsCalculator(int time, int frequency, double nominalInterest, double originalPrincipalSum, double accumulatedValue) throws JsonProcessingException {
        CompoundInterestRequestDto request = CompoundInterestRequestDto.builder()
                .time(time)
                .originalPrincipalSum(BigDecimal.valueOf(originalPrincipalSum))
                .compoundingFrequency(frequency)
                .nominalAnnualInterestRate(BigDecimal.valueOf(nominalInterest))
                .build();

        CompoundInterestWithDetailsResponseDto responseDto = interestService.compoundInterestCalculationWithDetails(request);

        assertThat(responseDto.getFinalAmount()).isEqualTo(BigDecimal.valueOf(accumulatedValue).setScale(2, RoundingMode.HALF_UP));
        assertThat(responseDto.getDetailedPeriods()).hasSize(time*frequency);
    }
}