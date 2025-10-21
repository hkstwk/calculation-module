package nl.hkstwk.calculationmodule.utils.calculators;

import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.dto.CompoundInterestDetailsDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CompoundInterestCalculator {

    public CompoundInterestResponseDto calculate(CompoundInterestRequestDto requestDto) {
        BigDecimal monthlyDeposit = requestDto.getMonthlyDeposit() != null ? requestDto.getMonthlyDeposit() : BigDecimal.ZERO;
        log.debug("Monthly deposit set to: {}", monthlyDeposit);

        BigDecimal periodicRate = requestDto.getNominalAnnualInterestRate().divide(BigDecimal.valueOf(requestDto.getCompoundingFrequency()), 10, RoundingMode.HALF_UP);
        log.debug("Calculated periodic rate: {}", periodicRate);

        int totalPeriods = requestDto.getCompoundingFrequency() * requestDto.getTime();
        log.debug("Total number of periods: {}", totalPeriods);

        BigDecimal accumulatedValue = requestDto.getOriginalPrincipalSum();
        log.debug("Initial principal sum: {}", accumulatedValue);

        List<CompoundInterestDetailsDto> periodDetails = new ArrayList<>();


        for (int period = 1; period <= totalPeriods; period++) {
            log.debug("Processing period {}", period);

            if (requestDto.isDepositAtStart()) {
                accumulatedValue = getAccumulatedValue(requestDto, accumulatedValue, monthlyDeposit);
            }

            BigDecimal interestForPeriod = accumulatedValue.multiply(periodicRate).setScale(10, RoundingMode.HALF_UP);
            log.debug("Interest for period {}: {}", period, interestForPeriod);

            accumulatedValue = accumulatedValue.add(interestForPeriod).setScale(10, RoundingMode.HALF_UP);
            log.debug("New accumulated value after interest: {}", accumulatedValue);

            if (!requestDto.isDepositAtStart()) {
                accumulatedValue = getAccumulatedValue(requestDto, accumulatedValue, monthlyDeposit);
            }
            
            if (requestDto.isIncludeDetails()) {
                CompoundInterestDetailsDto.CompoundInterestDetailsDtoBuilder builder = CompoundInterestDetailsDto.builder()
                        .periodNumber(period)
                        .initialDeposit(getInitialDepositForPeriod(accumulatedValue, interestForPeriod))
                        .interestForPeriod(interestForPeriod.setScale(2, RoundingMode.HALF_UP))
                        .accumulatedValue(accumulatedValue.setScale(2, RoundingMode.HALF_UP));

                if (monthlyDeposit.compareTo(BigDecimal.ZERO) > 0) {
                    builder.monthlyDeposit(monthlyDeposit.setScale(2, RoundingMode.HALF_UP));
                }

                periodDetails.add(builder.build());
                log.debug("Added period {} details to the response", period);
            }
        }

        CompoundInterestResponseDto response = CompoundInterestResponseDto.builder()
                .finalAmount(accumulatedValue.setScale(2, RoundingMode.HALF_UP))
                .details(periodDetails)
                .build();

        log.debug("Final accumulated amount: {}", response.getFinalAmount());
        return response;
    }

    private static BigDecimal getInitialDepositForPeriod(BigDecimal accumulatedValue, BigDecimal interestForPeriod) {
             return accumulatedValue.subtract(interestForPeriod).setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal getAccumulatedValue(CompoundInterestRequestDto requestDto, BigDecimal accumulatedValue, BigDecimal monthlyDeposit) {
        String moment = requestDto.isDepositAtStart() ? "start" : "end";

        BigDecimal result = accumulatedValue.add(monthlyDeposit).setScale(10, RoundingMode.HALF_UP);
        log.debug("New accumulated value after adding {} monthly deposit at {} of period: {}", monthlyDeposit, moment, result);

        return result;
    }
}
