package nl.hkstwk.calculationmodule.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.hkstwk.calculationmodule.entities.CalculationRequestEntity;
import nl.hkstwk.calculationmodule.repositories.CalculationRequestRepository;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalculationRequestService {
    private final CalculationRequestRepository calculationRequestRepository;

    @Transactional
    public CalculationRequestEntity saveRequest(CalculationRequestEntity calculationRequestEntity) {
        return calculationRequestRepository.save(calculationRequestEntity);
    }
}
