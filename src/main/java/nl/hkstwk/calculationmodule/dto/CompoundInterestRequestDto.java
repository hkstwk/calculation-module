package nl.hkstwk.calculationmodule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CompoundInterestRequestDto {
    @NotNull
    @Positive
    BigDecimal originalPrincipalSum;
    @PositiveOrZero
    @JsonInclude(JsonInclude.Include.NON_NULL)
    BigDecimal monthlyDeposit;
    @NotNull
    BigDecimal nominalAnnualInterestRate;
    @NotNull
    @Min(1)
    int compoundingFrequency;
    @NotNull
    @Min(1)
    int time;
    boolean includeDetails;
}
