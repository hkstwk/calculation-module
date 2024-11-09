package nl.hkstwk.calculationmodule.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.entities.CalculationRequestEntity;
import nl.hkstwk.calculationmodule.enums.CalculationTypeEnum;
import nl.hkstwk.calculationmodule.exceptions.DtoNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CompoundInterestMapper {
    // Mapping between `type` values and DTO classes
    private static final Map<CalculationTypeEnum, Class<?>> dtoTypeMap = new HashMap<>();

    static {
        dtoTypeMap.put(CalculationTypeEnum.COMPOUND_INTEREST, CompoundInterestRequestDto.class);
        // Add other mappings as needed
    }

    private final ObjectMapper objectMapper;

    public CalculationRequestEntity toEntity(CompoundInterestRequestDto requestDto, CalculationTypeEnum calculationType) throws JsonProcessingException {
        CalculationRequestEntity calculationRequestEntity = new CalculationRequestEntity();

        String data = objectMapper.writeValueAsString(requestDto);
        calculationRequestEntity.setRequestData(data);
        calculationRequestEntity.setCalculationType(calculationType);

        return calculationRequestEntity;
    }

    public Object toDto(CalculationRequestEntity calculationRequestEntity) {
        CalculationTypeEnum calculationType = calculationRequestEntity.getCalculationType();
        String requestData = calculationRequestEntity.getRequestData();

        Class<?> dtoClass = dtoTypeMap.get(calculationType);

        if (dtoClass != null) {
            return objectMapper.convertValue(requestData, dtoClass);
        }

        throw new DtoNotFoundException("Unsupported calculation request type '%s'".formatted(calculationType));
    }
}
