package nl.hkstwk.calculationmodule.repositories;

import nl.hkstwk.calculationmodule.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserCrudRepository extends CrudRepository<User, UUID>{
    Optional<User> findByUsername(String username);
}
