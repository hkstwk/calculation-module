package nl.hkstwk.calculationmodule.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import nl.hkstwk.calculationmodule.dto.CompoundInterestDetailsDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import nl.hkstwk.calculationmodule.entities.CalculationRequestEntity;
import nl.hkstwk.calculationmodule.enums.CalculationTypeEnum;
import nl.hkstwk.calculationmodule.mappers.CompoundInterestMapper;
import nl.hkstwk.calculationmodule.services.CalculationRequestService;
import nl.hkstwk.calculationmodule.services.InterestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterestControllerTest {
    @InjectMocks
    private InterestController interestController;

    @Mock
    private InterestService interestService;

    @Mock
    private CalculationRequestService calculationRequestService;

    @Mock
    private CompoundInterestMapper compoundInterestMapper;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void compoundInterestCalculation() throws JsonProcessingException {
        CompoundInterestRequestDto requestDto = getCompoundInterestRequestDto(1, false);

        UUID expectedUUID = UUID.randomUUID();
        CalculationRequestEntity expectedEntity = new CalculationRequestEntity();
        expectedEntity.setId(expectedUUID);

        CompoundInterestResponseDto responseDto = CompoundInterestResponseDto.builder()
                .finalAmount(BigDecimal.valueOf(104.00))
                .build();

        when(compoundInterestMapper.toEntity(requestDto, CalculationTypeEnum.COMPOUND_INTEREST)).thenReturn(expectedEntity);
        when(calculationRequestService.saveRequest(expectedEntity)).thenReturn(expectedEntity);
        when(interestService.compoundInterestCalculation(requestDto)).thenReturn(responseDto);

        ResponseEntity<CompoundInterestResponseDto> response = interestController.compoundInterestCalculation(requestDto);

        verify(calculationRequestService, times(1)).saveRequest(any(CalculationRequestEntity.class));
        verify(interestService, times(1)).compoundInterestCalculation(any(CompoundInterestRequestDto.class));

        assertNotNull(response);
        assertEquals(responseDto, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void compoundInterestWithDetailsCalculation() throws JsonProcessingException {
        CompoundInterestRequestDto requestDto = getCompoundInterestRequestDto(12, true);

        UUID expectedUUID = UUID.randomUUID();
        CalculationRequestEntity expectedEntity = new CalculationRequestEntity();
        expectedEntity.setId(expectedUUID);

        CompoundInterestResponseDto expectedResponseDto = CompoundInterestResponseDto.builder()
                .finalAmount(BigDecimal.valueOf(106.00))
                .details(List.of(
                        CompoundInterestDetailsDto.builder()
                                .interestForPeriod(BigDecimal.valueOf(1))
                                .accumulatedValue(BigDecimal.valueOf(101.00))
                                .startingAmount(BigDecimal.valueOf(100.00))
                                .periodNumber(1)
                                .build(),
                        CompoundInterestDetailsDto.builder()
                                .interestForPeriod(BigDecimal.valueOf(2))
                                .accumulatedValue(BigDecimal.valueOf(103.00))
                                .startingAmount(BigDecimal.valueOf(101.00))
                                .periodNumber(2)
                                .build(),
                        CompoundInterestDetailsDto.builder()
                                .interestForPeriod(BigDecimal.valueOf(3))
                                .accumulatedValue(BigDecimal.valueOf(106.00))
                                .startingAmount(BigDecimal.valueOf(103.00))
                                .periodNumber(3)
                                .build()
                ))
                .build();

        when(compoundInterestMapper.toEntity(requestDto, CalculationTypeEnum.COMPOUND_INTEREST_WITH_DETAILS)).thenReturn(expectedEntity);
        when(calculationRequestService.saveRequest(expectedEntity)).thenReturn(expectedEntity);
        when(interestService.compoundInterestCalculation(requestDto)).thenReturn(expectedResponseDto);

        ResponseEntity<CompoundInterestResponseDto> responseEntity = interestController.compoundInterestCalculation(requestDto);

        verify(calculationRequestService, times(1)).saveRequest(any(CalculationRequestEntity.class));
        verify(interestService, times(1)).compoundInterestCalculation(any(CompoundInterestRequestDto.class));

        assertNotNull(responseEntity);
        assertEquals(expectedResponseDto, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testInvalidCompoundInterestRequestDto() {
        // Create an invalid DTO
        CompoundInterestRequestDto invalidDto = CompoundInterestRequestDto.builder()
                .originalPrincipalSum(BigDecimal.valueOf(-100)) // Invalid: not positive
                .nominalAnnualInterestRate(null)                // Invalid: null
                .compoundingFrequency(0)                        // Invalid: less than 1
                .time(-1)                                       // Invalid: less than 1
                .includeDetails(false)
                .build();

        // Validate DTO manually
        Set<ConstraintViolation<CompoundInterestRequestDto>> violations = validator.validate(invalidDto);

        assertFalse(violations.isEmpty(), "Expected validation violations but found none.");

        // Check specific violation message
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("originalPrincipalSum") && v.getMessage().contains("must be greater than 0")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nominalAnnualInterestRate") && v.getMessage().contains("must not be null")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("compoundingFrequency") && v.getMessage().contains("must be greater than or equal to 1")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("time") && v.getMessage().contains("must be greater than or equal to 1")));
    }

    private static CompoundInterestRequestDto getCompoundInterestRequestDto(int compoundingFrequency, boolean includeDetails) {
        return CompoundInterestRequestDto.builder()
                .originalPrincipalSum(BigDecimal.valueOf(100))
                .nominalAnnualInterestRate(BigDecimal.valueOf(0.04))
                .compoundingFrequency(compoundingFrequency)
                .time(1)
                .includeDetails(includeDetails)
                .build();
    }
}