package nl.hkstwk.calculationmodule.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import nl.hkstwk.calculationmodule.entities.CalculationRequestEntity;
import nl.hkstwk.calculationmodule.enums.CalculationTypeEnum;
import nl.hkstwk.calculationmodule.mappers.CompoundInterestMapper;
import nl.hkstwk.calculationmodule.services.CalculationRequestService;
import nl.hkstwk.calculationmodule.services.InterestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RequestMapping("/interest")
public class InterestController {

    private final InterestService interestService;
    private final CalculationRequestService calculationRequestService;
    private final CompoundInterestMapper compoundInterestMapper;

    @PostMapping("/compound")
    public ResponseEntity<CompoundInterestResponseDto> compoundInterestCalculation(@Valid @RequestBody CompoundInterestRequestDto compoundInterestRequestDto) throws JsonProcessingException {
        log.info("Received request to calculate compound interest: {}", compoundInterestRequestDto);

        CalculationTypeEnum calculationType = compoundInterestRequestDto.isIncludeDetails() ? CalculationTypeEnum.COMPOUND_INTEREST_WITH_DETAILS : CalculationTypeEnum.COMPOUND_INTEREST;

        log.info("Saving request info ...");
        CalculationRequestEntity savedEntity = calculationRequestService.saveRequest(compoundInterestMapper.toEntity(compoundInterestRequestDto, calculationType));
        log.info("Request info saved with id {}", savedEntity.getId());

        return ResponseEntity.ok(interestService.compoundInterestCalculation(compoundInterestRequestDto));
    }
}
