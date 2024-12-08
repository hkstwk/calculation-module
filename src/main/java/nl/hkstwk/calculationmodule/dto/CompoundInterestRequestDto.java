package nl.hkstwk.calculationmodule.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CompoundInterestRequestDto {
    @NotNull
    @Positive
    BigDecimal originalPrincipalSum;
    @NotNull
    BigDecimal nominalAnnualInterestRate;
    @NotNull
    @Min(1)
    int compoundingFrequency;
    @NotNull
    @Min(1)
    int time;
    boolean showDetails;
}
