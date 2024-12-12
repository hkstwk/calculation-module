package nl.hkstwk.calculationmodule.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.entities.CalculationRequestEntity;
import nl.hkstwk.calculationmodule.enums.CalculationTypeEnum;
import nl.hkstwk.calculationmodule.exceptions.DtoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompoundInterestMapperTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private CompoundInterestMapper compoundInterestMapper;

    private CompoundInterestRequestDto requestDto;
    private CalculationTypeEnum calculationType;
    private CalculationRequestEntity calculationRequestEntity;

    @BeforeEach
    void setUp() {
        requestDto = CompoundInterestRequestDto.builder().build();
        calculationType = CalculationTypeEnum.COMPOUND_INTEREST;
        calculationRequestEntity = new CalculationRequestEntity();
        calculationRequestEntity.setCalculationType(calculationType);
    }

    @Test
    void toEntity_ShouldMapToEntity_WhenValidInput() throws JsonProcessingException {
        String mockRequestData = "{\"principal\":1000,\"rate\":5,\"time\":2}";
        when(objectMapper.writeValueAsString(requestDto)).thenReturn(mockRequestData);

        CalculationRequestEntity result = compoundInterestMapper.toEntity(requestDto, calculationType);

        assertNotNull(result, "Resulting entity should not be null");
        assertEquals(mockRequestData, result.getRequestData(), "Request data should match JSON output from ObjectMapper");
        assertEquals(calculationType, result.getCalculationType(), "Calculation type should be set correctly");

        verify(objectMapper, times(1)).writeValueAsString(requestDto);
    }

    @Test
    void toEntity_ShouldThrowJsonProcessingException_WhenObjectMapperFails() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(requestDto)).thenThrow(JsonProcessingException.class);

        assertThrows(JsonProcessingException.class, () -> compoundInterestMapper.toEntity(requestDto, calculationType), "JsonProcessingException should be thrown when ObjectMapper fails");

        verify(objectMapper, times(1)).writeValueAsString(requestDto);
    }

    // New test for toDto method
    @Test
    void toDto_ShouldMapToDto_WhenValidInput() {
        String mockRequestData = "{\"principal\":1000,\"rate\":5,\"time\":2}";
        calculationRequestEntity.setRequestData(mockRequestData);
        when(objectMapper.convertValue(mockRequestData, CompoundInterestRequestDto.class)).thenReturn(requestDto);

        CompoundInterestRequestDto result = (CompoundInterestRequestDto) compoundInterestMapper.toDto(calculationRequestEntity);

        assertNotNull(result, "Resulting DTO should not be null");
        verify(objectMapper, times(1)).convertValue(mockRequestData, CompoundInterestRequestDto.class);
    }

    @Test
    void toDto_ShouldThrowDtoNotFoundException_WhenUnsupportedCalculationType() {
        calculationRequestEntity.setCalculationType(CalculationTypeEnum.UNKNOWN);
        calculationRequestEntity.setRequestData("{\"principal\":1000,\"rate\":5,\"time\":2}");

        DtoNotFoundException exception = assertThrows(DtoNotFoundException.class, () -> compoundInterestMapper.toDto(calculationRequestEntity), "DtoNotFoundException should be thrown for unsupported calculation type");

        assertTrue(exception.getMessage().contains("Unsupported calculation request type"),
                "Exception message should mention unsupported calculation type");
    }

    @Test
    void toDto_ShouldHandleNullRequestDataGracefully() {
        calculationRequestEntity.setRequestData(null);

        assertThrows(IllegalArgumentException.class, () -> compoundInterestMapper.toDto(calculationRequestEntity), "IllegalArgumentException should be thrown for null request data");

    }
}
