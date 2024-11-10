package nl.hkstwk.calculationmodule.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hkstwk.calculationmodule.dto.CompoundInterestRequestDto;
import nl.hkstwk.calculationmodule.entities.CalculationRequestEntity;
import nl.hkstwk.calculationmodule.enums.CalculationTypeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CalculationRequestRepositoryTest {
    @Autowired
    private CalculationRequestRepository calculationRequestRepository;

    @Test
    void findAllByCalculationType() {
        List<CalculationRequestEntity> calculationRequestEntities= new ArrayList<>();
        calculationRequestEntities.add(createCalculationRequestEntity(BigDecimal.valueOf(100.00), BigDecimal.valueOf(0.04), 1, 1, CalculationTypeEnum.COMPOUND_INTEREST));
        calculationRequestEntities.add(createCalculationRequestEntity(BigDecimal.valueOf(200.00), BigDecimal.valueOf(0.03), 1, 2, CalculationTypeEnum.COMPOUND_INTEREST_WITH_DETAILS));
        calculationRequestEntities.add(createCalculationRequestEntity(BigDecimal.valueOf(300.00), BigDecimal.valueOf(0.02), 1, 3, CalculationTypeEnum.COMPOUND_INTEREST));

        calculationRequestRepository.saveAll(calculationRequestEntities);

        Pageable pageable = Pageable.ofSize(5);

        Page<CalculationRequestEntity> foundEntities = calculationRequestRepository.findAllByCalculationType(CalculationTypeEnum.COMPOUND_INTEREST, pageable);

        assertThat(foundEntities).hasSize(2);
    }

    @Test
    void saveCompoundInterestRequest() {
        CalculationRequestEntity entity = createCalculationRequestEntity(BigDecimal.valueOf(100.00), BigDecimal.valueOf(0.04), 1, 1, CalculationTypeEnum.COMPOUND_INTEREST);

        CalculationRequestEntity savedEntity = calculationRequestRepository.save(entity);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getCalculationType()).isEqualTo(CalculationTypeEnum.COMPOUND_INTEREST);
        assertThat(savedEntity.getRequestData()).isEqualTo(entity.getRequestData());
    }

    private static CalculationRequestEntity createCalculationRequestEntity(BigDecimal originalPrincipalSum, BigDecimal nominalAnnualInterestRate,
                                                                           int compoundingFrequency, int time, CalculationTypeEnum calculationType) {
        CompoundInterestRequestDto requestDto = CompoundInterestRequestDto.builder()
                .originalPrincipalSum(originalPrincipalSum)
                .nominalAnnualInterestRate(nominalAnnualInterestRate)
                .compoundingFrequency(compoundingFrequency)
                .time(time)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String requestData;
        try {
            requestData = objectMapper.writeValueAsString(requestDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        CalculationRequestEntity entity = new CalculationRequestEntity();
        entity.setCalculationType(calculationType);
        entity.setRequestData(requestData);

        return entity;
    }

}