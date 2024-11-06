package nl.hkstwk.calculationmodule.repositories;

import nl.hkstwk.calculationmodule.entities.BaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestRepository extends JpaRepository<BaseRequest, UUID> {
}
