package nl.hkstwk.calculationmodule.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.dto.CompoundInterestResponseDto;
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
        CompoundInterestRequestDto requestDto = CompoundInterestRequestDto.builder()
                .originalPrincipalSum(BigDecimal.valueOf(100))
                .nominalAnnualInterestRate(BigDecimal.valueOf(0.04))
                .compoundingFrequency(1)
                .time(1)
                .build();

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
    void compoundInterestWithDetailsCalculation() {
    }
}