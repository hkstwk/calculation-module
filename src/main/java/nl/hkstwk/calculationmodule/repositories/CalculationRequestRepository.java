package nl.hkstwk.calculationmodule.repositories;

import nl.hkstwk.calculationmodule.entities.CalculationRequestEntity;
import nl.hkstwk.calculationmodule.enums.CalculationTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CalculationRequestRepository extends JpaRepository<CalculationRequestEntity, UUID> {
    Page<CalculationRequestEntity> findAllByCalculationType(CalculationTypeEnum calculationType, Pageable pageable);
}
