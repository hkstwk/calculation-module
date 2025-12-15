package nl.hkstwk.calculationmodule.mappers;

import lombok.RequiredArgsConstructor;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.entities.CalculationRequestEntity;
import nl.hkstwk.calculationmodule.enums.CalculationTypeEnum;
import nl.hkstwk.calculationmodule.exceptions.DtoNotFoundException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CompoundInterestMapper {
    private static final Map<CalculationTypeEnum, Class<?>> DTO_TYPE_BY_CALCULATION = Map.of(
            CalculationTypeEnum.COMPOUND_INTEREST, CompoundInterestRequestDto.class,
            CalculationTypeEnum.COMPOUND_INTEREST_WITH_DETAILS, CompoundInterestRequestDto.class
    );

    private static Class<?> resolveDtoClass(CalculationTypeEnum calculationType) {
        Class<?> dtoClass = DTO_TYPE_BY_CALCULATION.get(calculationType);
        if (dtoClass == null) {
            throw new DtoNotFoundException("Unsupported calculation request type '%s'".formatted(calculationType));
        }
        return dtoClass;
    }

    private final ObjectMapper objectMapper;

    public CalculationRequestEntity toEntity(CompoundInterestRequestDto requestDto, CalculationTypeEnum calculationType){
        CalculationRequestEntity calculationRequestEntity = new CalculationRequestEntity();

        String data = objectMapper.writeValueAsString(requestDto);
        calculationRequestEntity.setRequestData(data);
        calculationRequestEntity.setCalculationType(calculationType);

        return calculationRequestEntity;
    }

    public Object toDto(CalculationRequestEntity calculationRequestEntity) {
        CalculationTypeEnum calculationType = calculationRequestEntity.getCalculationType();
        String requestData = calculationRequestEntity.getRequestData();

        if (requestData == null) {
            throw new IllegalArgumentException("Calculation request data is null");
        }

        Class<?> dtoClass = resolveDtoClass(calculationType);
        return objectMapper.readValue(requestData, dtoClass);
    }

}
