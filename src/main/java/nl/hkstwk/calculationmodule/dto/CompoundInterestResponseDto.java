package nl.hkstwk.calculationmodule.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CompoundInterestResponseDto {
    private BigDecimal finalAmount;
    private List<CompoundInterestDetailsDto> details;
}



