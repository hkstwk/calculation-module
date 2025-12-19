package nl.hkstwk.calculationmodule.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import nl.hkstwk.calculationmodule.enums.CalculationTypeEnum;
import nl.hkstwk.calculationmodule.mappers.CompoundInterestMapper;
import nl.hkstwk.calculationmodule.services.CalculationRequestService;
import nl.hkstwk.calculationmodule.services.InterestService;
import nl.hkstwk.calculationmodule.services.UserService;
import nl.hkstwk.calculationmodule.utils.UserUtil;
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
    private final UserService userService;

    @PostMapping("/compound")
    public ResponseEntity<CompoundInterestResponseDto> compoundInterestCalculation(@Valid @RequestBody CompoundInterestRequestDto compoundInterestRequestDto) throws JsonProcessingException {
        CalculationTypeEnum calculationType = compoundInterestRequestDto.getIncludeDetails() ? CalculationTypeEnum.COMPOUND_INTEREST_WITH_DETAILS : CalculationTypeEnum.COMPOUND_INTEREST;
        userService.findCreateOrUpdateUser(UserUtil.fetchCurrentUserFromContext().getUsername());
        calculationRequestService.saveRequest(compoundInterestMapper.toEntity(compoundInterestRequestDto, calculationType));
        return ResponseEntity.ok(interestService.compoundInterestCalculation(compoundInterestRequestDto));
    }
}
