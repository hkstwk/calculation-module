package nl.hkstwk.calculationmodule.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.dto.CompoundInterestPeriodDetailDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestWithDetailsResponseDto;
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

    public CompoundInterestResponseDto compoundInterestCalculation(CompoundInterestRequestDto compoundInterestRequestDto) {
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

    public CompoundInterestWithDetailsResponseDto compoundInterestWithDetailsCalculation(CompoundInterestRequestDto compoundInterestRequestDto) throws JsonProcessingException {
        log.info("Start calculation of compound interest with detailed results for request: {}", compoundInterestRequestDto);

        BigDecimal principalSum = compoundInterestRequestDto.getOriginalPrincipalSum();
        BigDecimal nominalAnnualInterestRate = compoundInterestRequestDto.getNominalAnnualInterestRate();
        int compoundingFrequency = compoundInterestRequestDto.getCompoundingFrequency();
        int time = compoundInterestRequestDto.getTime();

        // Calculate the periodic nominalAnnualInterestRate
        BigDecimal periodicRate = nominalAnnualInterestRate.divide(BigDecimal.valueOf(compoundingFrequency), 10, RoundingMode.HALF_UP);
        log.info("Periodic interest rate: {}", periodicRate);

        int totalPeriods = compoundingFrequency * time;
        BigDecimal accumulatedValue = principalSum;
        List<CompoundInterestPeriodDetailDto> periodDetails = new ArrayList<>();

        // Iterate through each compounding period to collect details
        for (int period = 1; period <= totalPeriods; period++) {
            BigDecimal interestForPeriod = accumulatedValue.multiply(periodicRate).setScale(10, RoundingMode.HALF_UP);
            accumulatedValue = accumulatedValue.add(interestForPeriod).setScale(10, RoundingMode.HALF_UP);

            // Capture details for the current period
            CompoundInterestPeriodDetailDto periodDetail = CompoundInterestPeriodDetailDto.builder()
                    .periodNumber(period)
                    .startingAmount(accumulatedValue.subtract(interestForPeriod).setScale(2, RoundingMode.HALF_UP))
                    .interestForPeriod(interestForPeriod.setScale(2, RoundingMode.HALF_UP))
                    .accumulatedValue(accumulatedValue.setScale(2, RoundingMode.HALF_UP))
                    .build();
            periodDetails.add(periodDetail);
        }

        // Final accumulated value rounded to 2 decimal places
        BigDecimal finalAmount = accumulatedValue.setScale(2, RoundingMode.HALF_UP);

        // Build response with full details
        CompoundInterestWithDetailsResponseDto response = CompoundInterestWithDetailsResponseDto.builder()
                .originalPrincipalSum(principalSum)
                .nominalAnnualInterestRate(nominalAnnualInterestRate)
                .compoundingFrequency(compoundingFrequency)
                .time(time)
                .finalAmount(finalAmount)
                .detailedPeriods(periodDetails)
                .build();

        log.info("Final accumulated value after {} periods: {}", totalPeriods, finalAmount);
        log.info("Detailed calculation: {}", objectMapper.writeValueAsString(response));
        return response;
    }

}

