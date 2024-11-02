package nl.hkstwk.calculationmodule.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CompoundInterestPeriodDetailDto {
    private int periodNumber;
    private BigDecimal startingAmount;
    private BigDecimal interestForPeriod;
    private BigDecimal accumulatedValue;
}
