package nl.hkstwk.calculationmodule.repositories;

import nl.hkstwk.calculationmodule.entities.CalculationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CalculationRequestRepository extends JpaRepository<CalculationRequestEntity, UUID> {
}
