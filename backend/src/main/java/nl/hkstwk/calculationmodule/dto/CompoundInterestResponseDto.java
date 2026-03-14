package nl.hkstwk.calculationmodule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CompoundInterestResponseDto {
    private BigDecimal finalAmount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CompoundInterestDetailsDto> details;
}



