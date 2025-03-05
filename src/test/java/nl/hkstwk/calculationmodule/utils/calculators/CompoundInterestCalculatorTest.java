package nl.hkstwk.calculationmodule.utils.calculators;

import nl.hkstwk.calculationmodule.dto.CompoundInterestDetailsDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CompoundInterestCalculatorTest {

    @Test
    void testCalculateBasicScenario() {
        CompoundInterestRequestDto request = CompoundInterestRequestDto.builder()
                .originalPrincipalSum(BigDecimal.valueOf(1000))
                .nominalAnnualInterestRate(BigDecimal.valueOf(0.05)) // 5% annual interest rate
                .compoundingFrequency(12) // Monthly compounding
                .time(1) // 1 year
                .includeDetails(false)
                .build();

        CompoundInterestResponseDto response = CompoundInterestCalculator.calculate(request);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(1051.16), response.getFinalAmount()); // Expected value
        assertTrue(response.getDetails().isEmpty()); // No details should be added
    }

    @Test
    void testCalculateWithMonthlyDeposits() {
        CompoundInterestRequestDto request = CompoundInterestRequestDto.builder()
                .originalPrincipalSum(BigDecimal.valueOf(1000))
                .monthlyDeposit(BigDecimal.valueOf(100))
                .nominalAnnualInterestRate(BigDecimal.valueOf(0.05)) // 5% annual interest rate
                .compoundingFrequency(12) // Monthly compounding
                .time(1) // 1 year
                .includeDetails(false)
                .build();

        CompoundInterestResponseDto response = CompoundInterestCalculator.calculate(request);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(2285.55), response.getFinalAmount()); // Expected value
        assertTrue(response.getDetails().isEmpty()); // No details should be added
    }

    @Test
    void testCalculateWithDetailsEnabled() {
        CompoundInterestRequestDto request = CompoundInterestRequestDto.builder()
                .originalPrincipalSum(BigDecimal.valueOf(1000))
                .monthlyDeposit(BigDecimal.valueOf(100))
                .nominalAnnualInterestRate(BigDecimal.valueOf(0.05)) // 5% annual interest rate
                .compoundingFrequency(12) // Monthly compounding
                .time(1) // 1 year
                .includeDetails(true)
                .build();

        CompoundInterestResponseDto response = CompoundInterestCalculator.calculate(request);

        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(2285.55), response.getFinalAmount()); // Expected value
        assertFalse(response.getDetails().isEmpty());
        assertEquals(12, response.getDetails().size()); // 12 periods for monthly compounding

        // Verify the details of the first period
        CompoundInterestDetailsDto firstPeriod = response.getDetails().get(0);
        assertNotNull(firstPeriod);
        assertEquals(1, firstPeriod.getPeriodNumber());
        assertEquals(BigDecimal.valueOf(1050.00), firstPeriod.getAccumulatedValue()); // Example value
    }

    @Test
    void testCalculateWithZeroPrincipalAndZeroInterest() {
        CompoundInterestRequestDto request = CompoundInterestRequestDto.builder()
                .originalPrincipalSum(BigDecimal.ZERO)
                .monthlyDeposit(BigDecimal.ZERO)
                .nominalAnnualInterestRate(BigDecimal.ZERO)
                .compoundingFrequency(1)
                .time(1)
                .includeDetails(false)
                .build();

        CompoundInterestResponseDto response = CompoundInterestCalculator.calculate(request);

        assertNotNull(response);
        assertEquals(BigDecimal.ZERO, response.getFinalAmount()); // Total should be zero
        assertTrue(response.getDetails().isEmpty()); // No details should be added
    }

    @Test
    void testCalculateWithLargeValues() {
        CompoundInterestRequestDto request = CompoundInterestRequestDto.builder()
                .originalPrincipalSum(new BigDecimal("1000000000"))
                .monthlyDeposit(new BigDecimal("10000"))
                .nominalAnnualInterestRate(new BigDecimal("0.1")) // 10% annual interest rate
                .compoundingFrequency(12) // Monthly compounding
                .time(10) // 10 years
                .includeDetails(false)
                .build();

        CompoundInterestResponseDto response = CompoundInterestCalculator.calculate(request);

        assertNotNull(response);
        assertTrue(response.getFinalAmount().compareTo(new BigDecimal("2000000000")) > 0); // Expected to grow significantly
    }
}