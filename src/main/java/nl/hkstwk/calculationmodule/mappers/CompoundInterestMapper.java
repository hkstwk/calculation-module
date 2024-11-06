package nl.hkstwk.calculationmodule.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.entities.BaseRequest;
import nl.hkstwk.calculationmodule.entities.CompoundInterestRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CompoundInterestMapper {
    // Mapping between `type` values and DTO classes
    private static final Map<String, Class<?>> dtoTypeMap = new HashMap<>();

    static {
        dtoTypeMap.put("CompoundInterest", CompoundInterestRequestDto.class);
        // Add other mappings as needed
    }

    private final ObjectMapper objectMapper;

    public CompoundInterestRequest toEntity(CompoundInterestRequestDto requestDto) throws JsonProcessingException {
        CompoundInterestRequest compoundInterestRequest = new CompoundInterestRequest();

        String data = objectMapper.writeValueAsString(requestDto);
        compoundInterestRequest.setData(data);

        return compoundInterestRequest;
    }

    public Object toDto(BaseRequest entity) {
        String type = entity.getType();
        String data = entity.getData();

        Class<?> dtoClass = dtoTypeMap.get(type);
        if (dtoClass != null) {
            return objectMapper.convertValue(data, dtoClass);
        }

        throw new IllegalArgumentException("Unsupported request type: " + type);
    }
}
