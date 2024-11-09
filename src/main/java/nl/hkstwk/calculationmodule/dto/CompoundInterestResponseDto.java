package nl.hkstwk.calculationmodule.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CompoundInterestResponseDto {
    private BigDecimal finalAmount;
}



