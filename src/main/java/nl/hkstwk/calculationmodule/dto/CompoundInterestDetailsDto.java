package nl.hkstwk.calculationmodule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CompoundInterestDetailsDto {
    private int periodNumber;
    private BigDecimal initialDeposit;
    private BigDecimal interestForPeriod;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal monthlyDeposit;
    private BigDecimal accumulatedValue;
}
