package nl.hkstwk.calculationmodule.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestWithDetailsResponseDto;
import nl.hkstwk.calculationmodule.entities.CalculationRequestEntity;
import nl.hkstwk.calculationmodule.enums.CalculationTypeEnum;
import nl.hkstwk.calculationmodule.mappers.CompoundInterestMapper;
import nl.hkstwk.calculationmodule.services.CalculationRequestService;
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
    private final CalculationRequestService calculationRequestService;
    private final CompoundInterestMapper compoundInterestMapper;

    @PostMapping("/compound")
    public ResponseEntity<CompoundInterestResponseDto> compoundInterestCalculation(@RequestBody CompoundInterestRequestDto compoundInterestRequestDto) throws JsonProcessingException {
        log.info("Received request to calculate compound interest: {}", compoundInterestRequestDto);

        log.info("Saving request info ...");
        CalculationRequestEntity savedEntity = calculationRequestService.saveRequest(compoundInterestMapper.toEntity(compoundInterestRequestDto, CalculationTypeEnum.COMPOUND_INTEREST));
        log.info("Request info saved with id {}", savedEntity.getId());

        return ResponseEntity.ok(interestService.compoundInterestCalculation(compoundInterestRequestDto));
    }

    @PostMapping("/compound-with-details")
    public ResponseEntity<CompoundInterestWithDetailsResponseDto> compoundInterestWithDetailsCalculation(@RequestBody CompoundInterestRequestDto compoundInterestRequestDto) throws JsonProcessingException {
        log.info("Received request to calculate compound interest: {}", compoundInterestRequestDto);

        log.info("Saving request info ...");
        CalculationRequestEntity savedEntity = calculationRequestService.saveRequest(compoundInterestMapper.toEntity(compoundInterestRequestDto, CalculationTypeEnum.COMPOUND_INTEREST_WITH_DETAILS));
        log.info("Request info saved with id {}", savedEntity.getId());

        return ResponseEntity.ok(interestService.compoundInterestCalculationWithDetails(compoundInterestRequestDto));
    }

}
