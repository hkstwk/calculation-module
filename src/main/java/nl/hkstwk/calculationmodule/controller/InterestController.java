package nl.hkstwk.calculationmodule.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import nl.hkstwk.calculationmodule.services.InterestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/interest")
public class InterestController {

    private final InterestService interestService;

    @PostMapping("/compound")
    public ResponseEntity<CompoundInterestResponseDto> compoundInterestCalculation(@RequestBody CompoundInterestRequestDto compoundInterestRequestDto) throws JsonProcessingException {
        log.info("Received request to calculate compound interest: {}", compoundInterestRequestDto);
        return ResponseEntity.ok(interestService.compoundInterestCalculation(compoundInterestRequestDto));
    }

    @PostMapping("/compound-with-details")
    public ResponseEntity<CompoundInterestResponseDto> compoundInterestWithDetailsCalculation(@RequestBody CompoundInterestRequestDto compoundInterestRequestDto) throws JsonProcessingException {
        log.info("Received request to calculate compound interest: {}", compoundInterestRequestDto);
        return ResponseEntity.ok(interestService.compoundInterestCalculationWithDetails(compoundInterestRequestDto));
    }

}
