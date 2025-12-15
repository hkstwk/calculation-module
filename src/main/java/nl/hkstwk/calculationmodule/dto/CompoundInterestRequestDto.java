package nl.hkstwk.calculationmodule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class CompoundInterestRequestDto {
    @NotNull
    @Positive
    BigDecimal originalPrincipalSum;
    @PositiveOrZero
    @JsonInclude(JsonInclude.Include.NON_NULL)
    BigDecimal monthlyDeposit;
    Boolean depositAtStart;
    @NotNull
    BigDecimal nominalAnnualInterestRate;
    @NotNull
    @Min(1)
    Integer compoundingFrequency;
    @NotNull
    @Min(1)
    Integer time;
    Boolean includeDetails;
}
