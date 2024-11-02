package nl.hkstwk.calculationmodule.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CompoundInterestResponseDto {
    private BigDecimal originalPrincipalSum;
    private BigDecimal nominalAnnualInterestRate;
    private int compoundingFrequency;
    private int time;
    private BigDecimal finalAmount;
    private List<CompoundInterestPeriodDetailDto> detailedPeriods;
}



