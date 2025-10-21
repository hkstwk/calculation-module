package nl.hkstwk.calculationmodule.utils.calculators;

import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class CompoundInterestCalculatorTest {
    private final CompoundInterestCalculator compoundInterestCalculator = new CompoundInterestCalculator();

    public static Stream<Arguments> compoundInterestCalculatorInput(){
        return Stream.of(
                arguments(1, 1, 0.03, 10_000.00, 0, 10_300.00),
                arguments(1, 12, 0.00, 10_000.00, 100, 11_200.00),
                arguments(1, 12, 0.03, 10_000.00, 100, 11_523.84),
                arguments(40, 12, 0.03, 200_000.00, 1_000, 1_591_404.40),
                arguments(1, 12, 0.03, 10_000.00, 0, 10_304.16),
                arguments(2, 1, 0.03, 10_000.00, 0, 10_609.00),
                arguments(2, 12, 0.03, 10_000.00, 0, 10_617.57),
                arguments(1, 1, 0.04, 10_000, 0, 10_400),
                arguments(1, 12, 0.04, 10_000, 0, 10_407.42),
                arguments(2, 12, 0.07, 10_000, 0, 11_498.06)
        );
    }

    @ParameterizedTest
    @MethodSource("compoundInterestCalculatorInput")
    public void testCompoundInterestCalculator(int time, int frequency, double nominalInterest, double originalPrincipalSum, double monthlyDeposit, double accumulatedValue) {
        CompoundInterestRequestDto request = CompoundInterestRequestDto.builder()
                .time(time)
                .originalPrincipalSum(BigDecimal.valueOf(originalPrincipalSum))
                .monthlyDeposit(BigDecimal.valueOf(monthlyDeposit))
                .depositAtStart(true)
                .compoundingFrequency(frequency)
                .nominalAnnualInterestRate(BigDecimal.valueOf(nominalInterest))
                .includeDetails(true)
                .build();

        CompoundInterestResponseDto responseDto = compoundInterestCalculator.calculate(request);

        assertThat(responseDto.getFinalAmount()).isEqualTo(BigDecimal.valueOf(accumulatedValue).setScale(2, RoundingMode.HALF_UP));
        assertThat(responseDto.getDetails()).hasSize(time*frequency);
    }

}