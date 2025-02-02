package nl.hkstwk.calculationmodule.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.dto.CompoundInterestDetailsDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestService {
    private final ObjectMapper objectMapper;

    public CompoundInterestResponseDto compoundInterestCalculationNoDetails(CompoundInterestRequestDto compoundInterestRequestDto) {
        log.info("Start calculation compound interest for request: {}", compoundInterestRequestDto);

        BigDecimal principalSum = compoundInterestRequestDto.getOriginalPrincipalSum();
        BigDecimal nominalAnnualInterestRate = compoundInterestRequestDto.getNominalAnnualInterestRate();
        int compoundingFrequency = compoundInterestRequestDto.getCompoundingFrequency();
        int time = compoundInterestRequestDto.getTime();

        // Calculate the periodic nominalAnnualInterestRate
        BigDecimal periodicRate = nominalAnnualInterestRate.divide(BigDecimal.valueOf(compoundingFrequency), 10, RoundingMode.HALF_UP).setScale(10, RoundingMode.HALF_UP);
        log.info("Periodic interest: {}", periodicRate);

        // Calculate the multiplier: (1 + periodicRate) ^ (frequency * time)
        BigDecimal base = BigDecimal.ONE.add(periodicRate);
        log.info("Base: {}", base);

        int exponent = compoundingFrequency * time;
        log.info("Exponent: {}", exponent);

        BigDecimal multiplier = base.pow(exponent).setScale(10, RoundingMode.HALF_UP);
        log.info("Multiplier: {}", multiplier);

        // Final amount after interest
        BigDecimal finalAmount = principalSum.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
        log.info("Accumulated value: {}", finalAmount);

        return CompoundInterestResponseDto.builder().finalAmount(finalAmount).build();
    }

    public CompoundInterestResponseDto compoundInterestCalculationWithDetails(CompoundInterestRequestDto compoundInterestRequestDto) throws JsonProcessingException {
        log.info("Start calculation of compound interest for request: {}", compoundInterestRequestDto);

        BigDecimal principalSum = compoundInterestRequestDto.getOriginalPrincipalSum();
        BigDecimal nominalAnnualInterestRate = compoundInterestRequestDto.getNominalAnnualInterestRate();
        int compoundingFrequency = compoundInterestRequestDto.getCompoundingFrequency();
        int time = compoundInterestRequestDto.getTime();
        BigDecimal monthlyDeposit = compoundInterestRequestDto.getMonthlyDeposit();
        if (monthlyDeposit == null) {
            monthlyDeposit = BigDecimal.ZERO;
        }

        BigDecimal periodicRate = nominalAnnualInterestRate.divide(BigDecimal.valueOf(compoundingFrequency), 10, RoundingMode.HALF_UP);
        log.info("Periodic interest rate: {}", periodicRate);

        int totalPeriods = compoundingFrequency * time;
        BigDecimal accumulatedValue = principalSum;
        List<CompoundInterestDetailsDto> periodDetails = new ArrayList<>();

        for (int period = 1; period <= totalPeriods; period++) {
            if (monthlyDeposit.compareTo(BigDecimal.ZERO) > 0 && ((period - 1) % (compoundingFrequency / 12)) == 0) {
                accumulatedValue = accumulatedValue.add(monthlyDeposit).setScale(10, RoundingMode.HALF_UP);
            }

            BigDecimal interestForPeriod = accumulatedValue.multiply(periodicRate).setScale(10, RoundingMode.HALF_UP);
            accumulatedValue = accumulatedValue.add(interestForPeriod).setScale(10, RoundingMode.HALF_UP);

            CompoundInterestDetailsDto.CompoundInterestDetailsDtoBuilder builder = CompoundInterestDetailsDto.builder()
                    .periodNumber(period)
                    .initialDeposit(accumulatedValue.subtract(interestForPeriod).setScale(2, RoundingMode.HALF_UP))
                    .interestForPeriod(interestForPeriod.setScale(2, RoundingMode.HALF_UP))
                    .accumulatedValue(accumulatedValue.setScale(2, RoundingMode.HALF_UP));

            // Include monthly deposit in details only if applicable
            if (monthlyDeposit.compareTo(BigDecimal.ZERO) > 0) {
                builder.monthlyDeposit(monthlyDeposit.setScale(2, RoundingMode.HALF_UP));
            }

            periodDetails.add(builder.build());
        }

        BigDecimal finalAmount = accumulatedValue.setScale(2, RoundingMode.HALF_UP);
        CompoundInterestResponseDto response = CompoundInterestResponseDto.builder()
                .finalAmount(finalAmount)
                .details(periodDetails)
                .build();

        log.info("Final accumulated value after {} periods: {}", totalPeriods, finalAmount);
        log.info("Detailed calculation: {}", objectMapper.writeValueAsString(response.getDetails()));
        return response;
    }
    public CompoundInterestResponseDto compoundInterestCalculation(@Valid CompoundInterestRequestDto compoundInterestRequestDto) throws JsonProcessingException {
        return compoundInterestRequestDto.isIncludeDetails() ?  compoundInterestCalculationWithDetails(compoundInterestRequestDto) : compoundInterestCalculationNoDetails(compoundInterestRequestDto);
    }
}

