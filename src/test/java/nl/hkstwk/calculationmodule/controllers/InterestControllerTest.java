package nl.hkstwk.calculationmodule.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.hkstwk.calculationmodule.dto.CompoundInterestPeriodDetailDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestWithDetailsResponseDto;
import nl.hkstwk.calculationmodule.entities.CalculationRequestEntity;
import nl.hkstwk.calculationmodule.enums.CalculationTypeEnum;
import nl.hkstwk.calculationmodule.mappers.CompoundInterestMapper;
import nl.hkstwk.calculationmodule.services.CalculationRequestService;
import nl.hkstwk.calculationmodule.services.InterestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void compoundInterestCalculation() throws JsonProcessingException {
        CompoundInterestRequestDto requestDto = getCompoundInterestRequestDto(1);

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
        CompoundInterestRequestDto requestDto = getCompoundInterestRequestDto(12);

        UUID expectedUUID = UUID.randomUUID();
        CalculationRequestEntity expectedEntity = new CalculationRequestEntity();
        expectedEntity.setId(expectedUUID);

        CompoundInterestWithDetailsResponseDto expectedResponseDto = CompoundInterestWithDetailsResponseDto.builder()
                .originalPrincipalSum(requestDto.getOriginalPrincipalSum())
                .nominalAnnualInterestRate(requestDto.getNominalAnnualInterestRate())
                .compoundingFrequency(requestDto.getCompoundingFrequency())
                .time(requestDto.getTime())
                .detailedPeriods(List.of(
                        CompoundInterestPeriodDetailDto.builder()
                                .interestForPeriod(BigDecimal.valueOf(1))
                                .accumulatedValue(BigDecimal.valueOf(101.00))
                                .startingAmount(BigDecimal.valueOf(100.00))
                                .periodNumber(1)
                                .build(),
                        CompoundInterestPeriodDetailDto.builder()
                                .interestForPeriod(BigDecimal.valueOf(2))
                                .accumulatedValue(BigDecimal.valueOf(103.00))
                                .startingAmount(BigDecimal.valueOf(101.00))
                                .periodNumber(2)
                                .build(),
                        CompoundInterestPeriodDetailDto.builder()
                                .interestForPeriod(BigDecimal.valueOf(3))
                                .accumulatedValue(BigDecimal.valueOf(106.00))
                                .startingAmount(BigDecimal.valueOf(103.00))
                                .periodNumber(3)
                                .build()
                ))
                .finalAmount(BigDecimal.valueOf(106.00))
                .build();

        when(compoundInterestMapper.toEntity(requestDto, CalculationTypeEnum.COMPOUND_INTEREST_WITH_DETAILS)).thenReturn(expectedEntity);
        when(calculationRequestService.saveRequest(expectedEntity)).thenReturn(expectedEntity);
        when(interestService.compoundInterestWithDetailsCalculation(requestDto)).thenReturn(expectedResponseDto);

        ResponseEntity<CompoundInterestWithDetailsResponseDto> responseEntity = interestController.compoundInterestWithDetailsCalculation(requestDto);

        verify(calculationRequestService, times(1)).saveRequest(any(CalculationRequestEntity.class));
        verify(interestService, times(1)).compoundInterestWithDetailsCalculation(any(CompoundInterestRequestDto.class));

        assertNotNull(responseEntity);
        assertEquals(expectedResponseDto, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    private static CompoundInterestRequestDto getCompoundInterestRequestDto(int compoundingFrequency) {
        return CompoundInterestRequestDto.builder()
                .originalPrincipalSum(BigDecimal.valueOf(100))
                .nominalAnnualInterestRate(BigDecimal.valueOf(0.04))
                .compoundingFrequency(compoundingFrequency)
                .time(1)
                .build();
    }
}