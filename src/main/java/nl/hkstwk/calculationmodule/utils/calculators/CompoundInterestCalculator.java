package nl.hkstwk.calculationmodule.utils.calculators;

import lombok.experimental.UtilityClass;
import nl.hkstwk.calculationmodule.dto.CompoundInterestDetailsDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CompoundInterestCalculator {

    public static CompoundInterestResponseDto calculate(CompoundInterestRequestDto requestDto){

        BigDecimal monthlyDeposit = requestDto.getMonthlyDeposit() != null ? requestDto.getMonthlyDeposit() : BigDecimal.ZERO;

        BigDecimal periodicRate = requestDto.getNominalAnnualInterestRate().divide(BigDecimal.valueOf(requestDto.getCompoundingFrequency()), 10, RoundingMode.HALF_UP);
        int totalPeriods = requestDto.getCompoundingFrequency() * requestDto.getTime();
        BigDecimal accumulatedValue = requestDto.getOriginalPrincipalSum();
        List<CompoundInterestDetailsDto> periodDetails = new ArrayList<>();

        for (int period = 1; period <= totalPeriods; period++) {
            if ((period - 1) % (12 / requestDto.getCompoundingFrequency()) == 0){
                accumulatedValue = accumulatedValue.add(monthlyDeposit).setScale(10, RoundingMode.HALF_UP);
            }

            BigDecimal interestForPeriod = accumulatedValue.multiply(periodicRate).setScale(10, RoundingMode.HALF_UP);
            accumulatedValue = accumulatedValue.add(interestForPeriod).setScale(10, RoundingMode.HALF_UP);

            if (requestDto.isIncludeDetails()) {
                CompoundInterestDetailsDto.CompoundInterestDetailsDtoBuilder builder = CompoundInterestDetailsDto.builder()
                        .periodNumber(period)
                        .initialDeposit(accumulatedValue.subtract(interestForPeriod).setScale(2, RoundingMode.HALF_UP))
                        .interestForPeriod(interestForPeriod.setScale(2, RoundingMode.HALF_UP))
                        .accumulatedValue(accumulatedValue.setScale(2, RoundingMode.HALF_UP));

                if (monthlyDeposit.compareTo(BigDecimal.ZERO) > 0) {
                    builder.monthlyDeposit(monthlyDeposit.setScale(2, RoundingMode.HALF_UP));
                }

                periodDetails.add(builder.build());
            }
        }

        return CompoundInterestResponseDto.builder()
                .finalAmount(accumulatedValue.setScale(2, RoundingMode.HALF_UP))
                .details(periodDetails)
                .build();
    }
}
