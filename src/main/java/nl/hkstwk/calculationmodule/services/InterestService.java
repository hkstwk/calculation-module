package nl.hkstwk.calculationmodule.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import nl.hkstwk.calculationmodule.utils.calculators.CompoundInterestCalculator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterestService {
    private final CompoundInterestCalculator compoundInterestCalculator;
    private final ObjectMapper objectMapper;

    public CompoundInterestResponseDto compoundInterestCalculation(@Valid CompoundInterestRequestDto request) throws JsonProcessingException {
        CompoundInterestResponseDto result = compoundInterestCalculator.calculate(request);

        log.info("Final accumulated value: {}", result.getFinalAmount());
        if (request.getIncludeDetails()) {
            log.info("Details of calculation: {}", objectMapper.writeValueAsString(result.getDetails()));
        }

        return result;
    }
}